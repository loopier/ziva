// =====================================================================
// SuperCollider Workspace
// =====================================================================

Ziva.boot;
Ziva.loadSamples("~/loopier/samples");
(
~avl = DrumKit(\avl,
	kick: \avlkick, kickch:1,
	sn: \avlsn, snch:1,
	ch: \avlhh, ohch:1,
	oh: \avlhh, ohch:1,
	rim: \avlrim, rimch:1,
	bell: \avlbell, bellch:1,
	cl: \avlcl, clch:1,
);
~avl.ch.n(5);

~tri = DrumKit(\tri,
	kick: \trikick,
	sn: \trisn,
	ch: \trihh,
	oh: \trihh,
	rim: \tririm,
	bell: \tribell,
	cl: \tricl,
);

a = Pbind(\instrument, \acid);
)
DrumPattern.directory;
TempoClock.default.tempo = 128/60;
(
[
	// a,
	~tri.rhythm(\electro2a),
].ziva
)
~dk.kick.patternpairs
~dk.rhythm(\house);
DrumPattern.at(\house).kick
DrumKit()
