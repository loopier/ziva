(
SynthDef(\fx_output_busses, { |amp, pan, out|
	var sig = \sig.ar;
	var chorusBus = \chorusBus.kr(0);
	var chorus = \chorus.kr(0);
	var delayBus = \delayBus.kr(0);
	var delay = \delay.kr(0);
	var reverbBus = \reverbBus.kr(0);
	var reverb = \reverb.kr(0);
	var chorusSig;
	var delaySig;
	var reverbSig;
	chorusSig = sig * chorus;
	delaySig = sig * delay;
	reverbSig = sig * reverb;
	sig = sig - chorusSig - delaySig - reverbSig;
	OffsetOut.ar(out, sig);
	OffsetOut.ar(chorusBus, chorusSig);
	OffsetOut.ar(delayBus, delaySig);
	OffsetOut.ar(reverbBus, reverbSig);
}).add;

SynthDef(\adsr, {
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.dadsr(Rand(0,0.007), attack, decay, sustain, release), gate, doneAction:2);
}).add;

// oscillators
SynthDef(\white, {
	var out = \out.kr(0);
	var chorusBus = \chorusBus.kr(0);
	var chorus = \chorus.kr(0);
	var delayBus = \delayBus.kr(0);
	var delay = \delay.kr(0);
	var reverbBus = \reverbBus.kr(0);
	var reverb = \reverb.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = WhiteNoise.ar(amp) * env;
	Out.ar(out, Pan2.ar(sig, pan));
	Out.ar(chorusBus, sig * chorus);
	Out.ar(delayBus, sig * delay);
	Out.ar(reverbBus, sig * reverb);
}).add;

SynthDef(\pink, {
	var out = \out.kr(0);
	var chorusBus = \chorusBus.kr(0);
	var chorus = \chorus.kr(0);
	var delayBus = \delayBus.kr(0);
	var delay = \delay.kr(0);
	var reverbBus = \reverbBus.kr(0);
	var reverb = \reverb.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = PinkNoise.ar(amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
	Out.ar(out, Pan2.ar(sig, pan));
	Out.ar(chorusBus, sig * chorus);
	Out.ar(delayBus, sig * delay);
	Out.ar(reverbBus, sig * reverb);
}).add;

SynthDef(\brown, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(1);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = BrownNoise.ar(amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\sine, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var feedback = \feedback.kr(0);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(0);
	var detune = \detune.kr(0) + SinOsc.ar(Rand(0,0.1));
	var freq = \freq.kr(440) + detune;
	var env = EnvGen.kr(Env.dadsr(Rand(0, 0.007), attack, decay, sustain, release), gate, doneAction:2);
	var sig = SinOscFB.ar(freq, feedback, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\tri, {
	var out = \out.kr(0);
	var chorusBus = \chorusBus.kr(0);
	var chorus = \chorus.kr(0);
	var delayBus = \delayBus.kr(0);
	var delay = \delay.kr(0);
	var reverbBus = \reverbBus.kr(0);
	var reverb = \reverb.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(0);
	var detune = \detune.kr(0) + SinOsc.ar(Rand(0,0.1));
	var freq = \freq.kr(440) + detune;
	var env = EnvGen.kr(Env.dadsr(Rand(0, 0.007), attack, decay, sustain, release), gate, doneAction:2);
	var sig = VarSaw.ar(freq, 0, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
	Out.ar(out, Pan2.ar(sig * env, pan));
	Out.ar(out, Pan2.ar(sig, pan));
	Out.ar(chorusBus, sig * chorus);
	Out.ar(delayBus, sig * delay);
	Out.ar(reverbBus, sig * reverb);
}).add;

SynthDef(\saw, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(0);
	var detune = \detune.kr(0) + SinOsc.ar(Rand(0,0.1));
	var freq = \freq.kr(440) + detune;
	var env = EnvGen.kr(Env.dadsr(Rand(0, 0.007), attack, decay, sustain, release), gate, doneAction:2);
	var sig = Saw.ar(freq, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\pulse, {
	var out = \out.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var pan = \pan.kr(0);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(0);
	var detune = \detune.kr(0) + SinOsc.ar(Rand(0,0.1));
	var freq = \freq.kr(440) + detune;
	var env = EnvGen.kr(Env.dadsr(Rand(0, 0.007), attack, decay, sustain, release), gate, doneAction:2);
	var sig = Pulse.ar(freq, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\impulse, {
	var out = \out.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(1);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = Impulse.ar(freq, width, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;

SynthDef(\lfpar, {
	var out = \out.kr(0);
	var amp = \amp.kr(1) * (-0.dbamp);
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
	var amp = \amp.kr(1) * (-0.dbamp);
	var pan = \pan.kr(0);
	var freq = \freq.kr(440);
	var width = \width.kr(0.5);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.01);
	var decay = \dec.kr(0.3);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0);
	var env = EnvGen.kr(Env.adsr(attack, decay, sustain, release), gate, doneAction:2);
	var sig = EnvGen.ar(Env.adsr(attack, decay, sustain, release), gate, levelScale: amp, doneAction:2);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;
)
