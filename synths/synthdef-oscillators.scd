(
SynthDef(\white, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = WhiteNoise.ar(amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\pink, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = PinkNoise.ar(amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\brown, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = BrownNoise.ar(amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\sine, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var lag = \lag.kr(0);
	var freq = \freq.kr(440).lag(lag);
	var feedback = \feedback.kr(0);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = SinOscFB.ar(freq * [1,1.005], feedback, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\tri, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var lag = \lag.kr(0);
	var freq = \freq.kr(440).lag(lag);
	var feedback = \feedback.kr(0);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = LFTri.ar(freq * [1,1.005], mul: amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\saw, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var lag = \lag.kr(0);
	var freq = \freq.kr(440).lag(lag);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Saw.ar(freq * [1,1.005], amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\pulse, {
	var out = \out.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var pan = \pan.kr(0);
	var lag = \lag.kr(0);
	var freq = \freq.kr(440).lag(lag);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Pulse.ar(freq * [1,1.005], width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\impulse, {
	var out = \out.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Impulse.ar(freq, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\lfpar, {
	var out = \out.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = LFPar.ar(freq, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\env, {
	var out = \out.kr(0);
	var amp = \amp.kr(1);// * (-12.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = EnvGen.ar(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;
)
