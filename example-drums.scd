// =====================================================================
// To run this example you will need a set of samples for every drum part.
// You will also need to install DrumPatterns that can be found as a
// quark: Quarks.install("https://github.com/loopier/drumpattern")
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
)
// list all available drum pattern names
DrumPattern.directory;
TempoClock.default.tempo = 128/60;
(
[
	// the symbol is the DrumPattern name
	~tri.rhythm(\house),
	// shortcut: ~tri.r(\house),
].ziva
)
~dk.kick.patternpairs
~dk.rhythm(\house);
DrumPattern.at(\house).kick
DrumKit()
