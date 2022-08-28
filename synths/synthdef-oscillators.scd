(
SynthDef(\white, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-12.dbamp);
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
	var amp = \amp.kr(1) * (-12.dbamp);
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
	var amp = \amp.kr(1) * (-12.dbamp);
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
	var amp = \amp.kr(1) * (-12.dbamp);
	var freq = \freq.kr(440);
	var feedback = \feedback.kr(0);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = SinOscFB.ar(freq, feedback, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\saw, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-12.dbamp);
	var freq = \freq.kr(440);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Saw.ar(freq, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\pulse, {
	var out = \out.kr(0);
	var amp = \amp.kr(1) * (-12.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Pulse.ar(freq, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\fmop, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-12.dbamp);
	var freq = \freq.kr(440);
	var ratio = \ratio.kr(1);
	var mod = \in.ar(0);
	var modamt = \modamt.ar(1);
	var gate = \gate.kr(1);
	var atk = \atk.kr(0.01);
	var dec = \dec.kr(0.3);
	var sus = \sus.kr(1.0);
	var rel = \rel.kr(0.0);
	var atkl = \atkl.kr(1.0);
	var decl = \decl.kr(1.0);
	// var susl = \susl.kr(1);
	var rell = \rell.kr(0.0);
	// var atkt = \atkt.kr(0.01);
	// var dect = \dect.kr(1);
	var sust = \sust.kr(1.0);
	// var relt = \relt.kr(0);
	// var env = EnvGen.kr(Env.adsr(atk, dec, sus, rel), gate, doneAction:2);
	var ads = [dec, sust].normalizeSum * \sustain.kr(1.0);
	var env = EnvGen.kr(Env.new([0, atkl, decl, sus, rell],[atk, ads[0], ads[1], rel], \lin), gate, doneAction:2);
	var sig = SinOsc.ar(freq * ratio, mod * modamt, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;
)
