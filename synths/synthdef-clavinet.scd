(
SynthDef(\clavinet, {
	var feedback = \feedback.kr(0.8);
	var amp = \amp.kr(0.3);
	var spread = \spread.kr(0.8);
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var gate = \gate.kr(1);
	var freq = \freq.kr(440);
	var decay = \dec.kr(0.3);
	var sus = \sust.kr(0.3);
	var env = EnvGen.ar(Env.adsr(\atk.kr(0.01), decay, \sus.kr(0.5), \rel.kr(1)), gate, doneAction:2);
	var freqs = Array.fill(6, { |i| (\freq++(i+1)).asSymbol.kr(1)});

	var envs = [
		EnvGen.kr(Env.new([0,1,1,1,0],[0.001,0.3,0.5 * sus,0.01 * decay], gate, 3,1)),
		EnvGen.kr(Env.new([0,1,0.1,0.5,0],[0.001,0.3,0.5 * sus,0 * decay], gate, 3,1)),
		EnvGen.kr(Env.new([0,1,0.2,0.7,0.7],[0.001,0.5,0.1 * sus,0 * decay], gate, 3,1)),
		EnvGen.kr(Env.new([0,1,0.5,0.5,0],[0.001,0.6,0.1 * sus,0 * decay], gate, 3,1)),
		EnvGen.kr(Env.new([0,1,0.9,0.9,0.9],[0.001,0.3,0.5 * sus,0.5 * decay], gate, 3,1)),
		EnvGen.kr(Env.new([0,1,0.6,0.4,0],[0.001,0.001,0.9 * sus, 0 * decay], gate, 3,1)),
	];

	var ctls = [
		[freq * 4 - 1, 0, envs[0]],
		[freq + 1, 0, envs[1]],
		[1.62181, 0, envs[2]],
		[freq * 7 - 2, 0, envs[3]],
		[freq * 3 + 3, 0, envs[4]],
		[freq * 11, 0, envs[5]],
	];

	var mods = [
		[0,1,0,0,0,0],
		[0,0,0,0,0,0],
		[0,0,0,1,1,0],
		[0,0,0,0,0,0],
		[0,0,0,0,0,1],
		[0,0,0,0,0,1],
	];

	var sig = FM7.ar(ctls, mods, feedback).slice([0,2]);
	sig = RLPF.ar(sig, freq.linexp(1,10000,3000,6000), 0.1);
	// sig = RLPF.ar(sig, freq.linexp(1,10000,6000,3000), 0.1);
	// sig = RHPF.ar(sig, freq.linexp(1,10000,300,6000), 0.1);
	Out.ar(out, Pan2.ar(Mix.ar(sig * amp * env), pan));
}).add;
)
// Pbindef(\a, \instrument, \clavinet, \octave, Pbrown(2,4,1), \degree, [0,2,1,4].pseq(inf) + [0,[0,4,7]].prand(inf), \dur, [1,2,4].prand(inf)/8, \legato, [0.1, 0.5, 1, 1.5].prand(inf)).play
// Pbindef(\a, \instrument, \clavinet, \octave, [[3,2],[3,4],[4,5]].prand(inf), \dur, 4, \legato, 1).play
// Pbindef(\a, \dur, 1/2, \strum, 0.025, \degree, [0,2,4])