(
SynthDef(\reslab, {
	var freq = \freq.kr(440);
	var amp = \amp.kr(1);
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var attackTime = \atk.kr(0.01);
	var decayTime = \dec.kr(0.01);
	var sustainLevel = \sus.kr(0.5);
	var releaseTime = \rel.kr(0.5);
	var gate = \gate.kr(1);
	var cutoff = \cutoff.kr(1000);
	var rq = \rq.kr(0.1);

	var sig, env, filter;
	env = Env.adsr(attackTime, decayTime, sustainLevel, releaseTime).kr(2, gate);
	// env = EnvGen.kr(Env.perc(attackTime, decayTime), gate, doneAction: 2);
	sig = Mix([Saw.ar(freq), Pulse.ar(freq / 2, 0.5)]);
	// sig = RLPF.ar(sig, LFNoise1.kr(0.3, 1000, 1500), rq, env);
	sig = RLPF.ar(sig, cutoff, rq, env);

	Out.ar(out, Pan2.ar(sig * env * amp * (-12.dbamp), pan));
}).add;
)

