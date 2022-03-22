// =====================================================================
// SuperCollider Workspace
// =====================================================================

Ziva.boot;

Ziva.track(0, \delay);
Ziva.track(1, \reverb);
(
a = Pbind(\instrument, \acid);
)
(
Pdef(\master, Ppar([
	a >> 0,
	a.fx(1),
	nil
])).play.quant_(1);
)
Ziva.track(0); // remove effect on track 0
Ziva.track(1);
Pdef.removeAll