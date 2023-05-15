(
SynthDef(\noise0, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);
	var gate = \gate.kr(1);
	var freq = \freq.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = LFNoise0.ar(freq, mul:amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\noise1, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);
	var gate = \gate.kr(1);
	var freq = \freq.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = LFNoise1.ar(freq, mul:amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\noise2, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);
	var gate = \gate.kr(1);
	var freq = \freq.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = LFNoise2.ar(freq, mul:amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\dust, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);
	var gate = \gate.kr(1);
	var freq = \freq.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Dust.ar(freq, mul:amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

)
