(
SynthDef(\shine, {
	var freq = \freq.kr(440);
	var amp = \amp.kr(0.3);
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var attackTime = \atk.kr(0.01);
	var decayTime = \dec.kr(0.3);
	var sustainLevel = \sus.kr(0.5);
	var releaseTime = \rel.kr(0.01);
	var gate = \gate.kr(1);

	var sig, env;
	sig = Saw.ar(freq * [0.9, 1, 1.1]);
	env = Env.adsr(attackTime, decayTime, sustainLevel, releaseTime).kr(2, gate);
	sig = LPF.ar(sig, freq);
	// sig = GVerb.ar(sig, roomsize: 10, revtime: 3, damping: 0.5, inputbw: 0.5, spread: 15, drylevel: 1, earlyreflevel: 0.7, taillevel: 0.5, mul: 0.5);
	// sig = JPverb.ar(sig);
	sig = FreeVerb.ar(sig, mix: 0.59, room: 0.8, damp: 0.1);

	Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add
);

// Synth(\shine)

// (
// var synthname = \shine;
// Pdef((synthname++"pat").asSymbol,
// 	Pbind(\instrument, synthname,
// 		// \degree, 0,
// 		// \dur, 1,
// 		// \attackTime, 0.01,
// 		// \decayTime, 0.3,
// 		// \sustainLevel, 0.5,
// 		// \releaseTime, 1,
// 		// \amp, 0.3,
// 	)
// ).play
// )
