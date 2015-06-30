#!/usr/bin/perl -w

# "Copyright (c) 2000-2002 The Regents of the University of California.  
# All rights reserved.
# 
# Permission to use, copy, modify, and distribute this software and its
# documentation for any purpose, without fee, and without written agreement is
# hereby granted, provided that the above copyright notice, the following
# two paragraphs and the author appear in all copies of this software.
# 
# IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
# DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
# OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
# CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
# 
# THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
# AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
# ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
# PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."

# Authors: Cory Sharp
# $Id: create_NeighborExt.pl,v 1.1 2003/06/02 12:34:18 dlkiskis Exp $

use strict;

use FindBin;
use lib $FindBin::Bin;
use FindInclude;
use SlurpFile;



my %opts = ( ncstdin => 0 );
my %sources = ();
my %exts = ( address => { type => "uint16_t", name => "address", init => "0", tupletype => 0 } );

my $G_warning =<< 'EOF';
// *** WARNING ****** WARNING ****** WARNING ****** WARNING ****** WARNING ***
// ***                                                                     ***
// *** This file was automatically generated by create_NeighborExt.pl.     ***
// *** Any and all changes made to this file WILL BE LOST!                 ***
// ***                                                                     ***
// *** WARNING ****** WARNING ****** WARNING ****** WARNING ****** WARNING ***

EOF

@ARGV = &FindInclude::parse_include_opts( @ARGV );
while( @ARGV && $ARGV[0] =~ /^-/ ) {
  my $opt = shift;
  if( $opt eq "-ncstdin" ) { $opts{ncstdin} = 1; }
  else { die "unknown option $opt\n"; }
}

if( $opts{ncstdin} ) {
  @ARGV = ();
  map { chomp; $sources{$_} = 1 } <>;
} else {
  map { $sources{$_} = 1 } @ARGV;
}


# process those filenames for the special lines
@ARGV = sort keys %sources;
while(<>) {
  if( /^\s*\/\/!!\s*Neighbor\s+(.*)/ ) {
    my $tt = $1;
    if( $tt =~ /(\d+)\s*\{\s*([^;=]*[^;=\s])\s+(\w+)(?:\s*=\s*([^;]+))?\s*;\s*\}\s*$/ ) {
      $exts{$3} = { tupletype => $1, type => $2, name => $3, init => $4 };
    } else {
      print STDERR "WARNING, malformed Neighbor line:\n  > $_";
    }
  }

}

# make build/
my $dir = "build";
mkdir $dir unless -d $dir;

create_neighbor_ext( $dir, \%exts );
create_publish_module( $dir, \%exts );
create_neighborhood_configuration( $dir, \%exts );

for my $ee (sort keys %exts) {
  if( $ee ne "address" ) {
    create_neighbor_interface( $dir, $exts{$ee} );
  }
}


#
sub filter_nesc {
  my ($infile,$outfile,$subs) = @_;
  my $file = &FindInclude::find_file( $infile );
  die "ERROR, could not find $infile, aborting.\n" unless defined $file;
  my $text = &SlurpFile::scrub_c_comments( &SlurpFile::slurp_file( $file ) );
  $text =~ s/^\s+//;
  my $err = "";
  $text =~ s/\$\{(\w+)\}(?:\s*?\n)?/
             $err.="$file: unknown Neighbor variable $1\n" unless defined $subs->{$1};
	     $subs->{$1}
	    /eg;
  die "${err}ERROR, unknown variables, aborting.\n" if $err;
  &SlurpFile::dump_file( $outfile, "$G_warning$text" );
  1;
}

sub create_neighbor_ext {
  my ($dir,$exts) = @_;
  $dir .= "/" unless $dir =~ m/\/$/;
  my %subs = ( fields => "", inits => "" );
  for my $ee (sort keys %{$exts}) {
    $subs{fields} .= "  $exts->{$ee}->{type} $exts->{$ee}->{name};\n";
    $subs{inits} .= "  $exts->{$ee}->{name} : $exts->{$ee}->{init},\n"
      if defined($exts{$ee}->{init}) && length($exts{$ee}->{init});
  }
  my $filename = "${dir}NeighborExt.h";
  print STDERR "    creating $filename\n";
  filter_nesc( "NeighborExt.neighbor.h", $filename, \%subs );
}


sub create_neighbor_interface {
  my ($dir, $ext) = @_;
  $dir .= "/" unless $dir =~ m/\/$/;
  my $name = $ext->{name};
  my $filename = "${dir}Neighbor_${name}.nc";
  print STDERR "    creating $filename\n";
  open FH, "> $filename" or die "ERROR, creating interface file, $!, aborting.\n";
  print FH <<"EOF";
$G_warning
interface Neighbor_$name
{
  command void set( uint16_t address, $ext->{type}* $ext->{name} );
  command void requestRemoteTuples();
  command void publish();
  event void updatedFromRemote( uint16_t address );
  command NeighborPtr_t getByAddress(uint16_t address);
  command TupleIterator_t initIterator();
  command bool getNext(TupleIterator_t* iterator);

}

EOF
  close FH;
}


sub create_publish_module {
  my ($dir, $exts) = @_;
  $dir .= "/" unless $dir =~ m/\/$/;

  my %subs = (
    send_cases => "",
    receive_cases => "",
    neighbor_funcs => "",
    provides => "",
  );

  for my $ee (sort { $exts->{$a}->{tupletype} <=> $exts->{$b}->{tupletype} } keys %{$exts}) {
    next if $exts->{$ee}->{name} eq "address";

    my $case = "case $exts->{$ee}->{tupletype}:";
    my $type = $exts->{$ee}->{type};
    my $name = $exts->{$ee}->{name};

    $subs{send_cases} .= <<"EOF";
      $case // $name
        if( (msgdata = initRoutingMsg( &m_msg, sizeof($type) )) == 0 )
	  return;
	*($type*)msgdata = nn->$name;
	break;

EOF

    $subs{receive_cases} .= <<"EOF";
      $case // $name
        if( (msgdata = popFromRoutingMsg( msg, sizeof($type) )) == 0 )
	  return msg;
	call Neighbor_$name.set( head->address, ($type*)msgdata );
	signal Neighbor_$name.updatedFromRemote( head->address );
	break;

EOF

    $subs{provides} .= "\n    interface Neighbor_$name;";

    $subs{neighbor_funcs} .= <<"EOF";
  command void Neighbor_$name.set( uint16_t address, $type* $name )
  {
    Neighbor_t* nn = call TupleStore.privateGetByAddress( address );

    if( nn != 0 )
    {
      nn->$name = *$name;
    }
    else
    {
      nn = call TupleManager.getByAddress( address );
      nn->$name = *$name;
      call TupleManager.setTuple( nn );
    }

    call TuplePublisher.publish( $exts->{$ee}->{tupletype}, address );
  }

  command void Neighbor_$name.requestRemoteTuples()
  {
      requestRemoteTuples( $exts->{$ee}->{tupletype});
  }

  command void Neighbor_$name.publish()
  {
    call TuplePublisher.publish( $exts->{$ee}->{tupletype}, TOS_LOCAL_ADDRESS );
  }


  default event void Neighbor_$name.updatedFromRemote( uint16_t address )
  {
  }

  command NeighborPtr_t Neighbor_$name.getByAddress(uint16_t address)
  {
      return call TupleStore.getByAddress(address);
  }

  command TupleIterator_t Neighbor_$name.initIterator()
  {
      return call TupleStore.initIterator();
  }

  command bool Neighbor_$name.getNext(TupleIterator_t* iterator)
  {
      return call TupleStore.getNext(iterator);
  }

EOF
  }

  my $filename = "${dir}TuplePublisherM.nc";
  print STDERR "    creating $filename\n";
  filter_nesc( "TuplePublisherM.neighbor.nc", $filename, \%subs );
}


sub create_neighborhood_configuration {
  my ($dir, $exts) = @_;
  $dir .= "/" unless $dir =~ m/\/$/;

  my %subs = ( provides => "", wiring => "" );
  for my $ee (sort keys %{$exts}) {
    my $name = $exts->{$ee}->{name};
    next if $name eq "address";
    my $iface = "Neighbor_${name}";
    $subs{provides} .= "    interface ${iface};\n";
    $subs{wiring} .= "  ${iface} = TuplePublisherM.${iface};\n";
  }

  my $filename = "${dir}NeighborhoodC.nc";
  print STDERR "    creating $filename\n";
  filter_nesc( "NeighborhoodC.neighbor.nc", $filename, \%subs );
}


