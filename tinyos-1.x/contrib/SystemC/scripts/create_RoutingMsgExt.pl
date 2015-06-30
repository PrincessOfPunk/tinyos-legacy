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
# $Id: create_RoutingMsgExt.pl,v 1.1 2003/10/09 01:14:18 cssharp Exp $

use strict;

my %opts = ();
my %sources = ();
my %exts = ();

while( @ARGV && $ARGV[0] =~ /^-/ ) {
  my $opt = shift;
  if( $opt eq "-list-sources" ) { $opts{sources} = 1; }
  elsif( $opt eq "-nc" ) { $opts{nc} = 1; }
  elsif( $opt eq "-ncstdin" ) { $opts{ncstdin} = 1; }
  else { die "unknown option $opt\n"; }
}

if( $opts{nc} ) {
  map { $sources{$_} = 1 } @ARGV;
} elsif( $opts{ncstdin} ) {
  @ARGV = ();
  map { chomp; $sources{$_} = 1 } <>;
} else {
  # extract all "# \d+ ..." filenames from app.c
  while(<>) { $sources{$1} = 1 if /^#\s+\d+\s+"([^"]+)"/; }
}

if( $opts{sources} ) {
  print join("\n",sort keys %sources), "\n";
  exit;
}

# now process those filenames for the special lines
@ARGV = sort keys %sources;
while(<>) {
  if( /^\s*\/\/!!\s*RoutingMsgExt\s*\{(.*)\}\s*$/ ) {
    my $tt = $1;
    #for my $ee (split( /\s*;\s*/, $tt)) {
    #  $exts{$1} = $ee if $ee =~ /(\w+)$/;
    #}
    while( $tt =~ m/\s*([^;=]+\s+(\w+))(?:(?:\s*=\s*)([^;]+))?\s*;/g ) {
      $exts{$2} = { decl => $1, name => $2, init => $3 };
    }
  }
}

# now dump RoutingMsgExt.h to stdout
my $are_all_fields_init = 1;
my ($fields, $structinits, $funcinits) = ("","","");
for my $ee (sort keys %exts) {
  $fields .= "  $exts{$ee}->{decl};\n";
  my $name = $exts{$ee}->{name};
  if( defined $exts{$ee}->{init} ) {
    $structinits .= "  $name : $exts{$ee}->{init},\n";
    $funcinits .= "  ext->$name = G_DefaultRoutingMsgExt.$name;\n";
  } else {
    $are_all_fields_init = 0;
  }
}

if( $are_all_fields_init ) {
  $funcinits = "  *ext = G_DefaultRoutingMsgExt;\n";
}

my $filename = "build/RoutingMsgExt.h";
print STDERR "    creating $filename\n";
open FH, "> $filename" or die "ERROR, creating $filename, $!\n";
print FH <<"EOF";
// *** WARNING ****** WARNING ****** WARNING ****** WARNING ****** WARNING ***
// ***                                                                     ***
// *** This file was automatically generated by create_RoutingMsgExt.pl.   ***
// *** Any and all changes made to this file WILL BE LOST!                 ***
// ***                                                                     ***
// *** WARNING ****** WARNING ****** WARNING ****** WARNING ****** WARNING ***

#ifndef _H_RoutingMsgExt_h
#define _H_RoutingMsgExt_h

typedef struct {
$fields} RoutingMsgExt_t;

const RoutingMsgExt_t G_DefaultRoutingMsgExt = {
$structinits};

void initRoutingMsgExt( RoutingMsgExt_t* ext )
{
$funcinits}

#endif // _H_RoutingMsgExt_h

EOF
close FH;

