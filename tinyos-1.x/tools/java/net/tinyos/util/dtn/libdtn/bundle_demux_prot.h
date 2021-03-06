/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _BUNDLE_DEMUX_PROT_H_RPCGEN
#define _BUNDLE_DEMUX_PROT_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


#define BUNDLE_DEMUX_PROG 200200
#define BUNDLE_DEMUX_VERS 1

#if defined(__STDC__) || defined(__cplusplus)
#define BUNDLE_ARRIVED 1
extern  BUNDLE_RESULT * bundle_arrived_1(BUNDLE_WAITING *, CLIENT *);
extern  BUNDLE_RESULT * bundle_arrived_1_svc(BUNDLE_WAITING *, struct svc_req *);
extern int bundle_demux_prog_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define BUNDLE_ARRIVED 1
extern  BUNDLE_RESULT * bundle_arrived_1();
extern  BUNDLE_RESULT * bundle_arrived_1_svc();
extern int bundle_demux_prog_1_freeresult ();
#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_BUNDLE_DEMUX_PROT_H_RPCGEN */
