// =====================================================================
// SuperCollider Workspace
// =====================================================================

Ziva.boot;
Ziva.loadSamples("~/loopier/samples");

Ziva.track(0, \delay);
Ziva.track(1, \reverb);
(
a = Pbind(\instrument, \acid);
h = Psample(\aewaw, 1);
)
(
Pdef(\master, Ppar([
	a >> 0,
	h.fx(1),
	nil
])).play.quant_(1);
)
Ziva.track(0); // remove effect on track 0
Ziva.track(1);
Pdef.removeAll