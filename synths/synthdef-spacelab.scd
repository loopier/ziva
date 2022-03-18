(
SynthDef(\kwbd, { | out=0  amp=1|
	var osc, env;
	osc = FSinOsc.ar(40) * amp;
	env = EnvGen.kr(Env.perc(0, 0.05), doneAction: 2);
	Out.ar(out, Pan2.ar(osc, 0, env));
}).add;

SynthDef(\kwsn, { | out=0  amp=1|
	var osc1, osc2, env;
	osc1 = WhiteNoise.ar;
	osc2 = FSinOsc.ar(200);
	env = EnvGen.kr(Env.perc(0, 0.05), doneAction: 2);
	Out.ar(out, Pan2.ar(LPF.ar(Mix([osc1, osc2] * amp), 12000), 0, env));
}).add;

SynthDef(\kwhat, { | out=0 amp=1|
	var osc1, env;
	osc1 = WhiteNoise.ar(amp);
	env = EnvGen.kr(Env.perc(0, 0.01), doneAction: 2);
	Out.ar(out, Pan2.ar(osc1, 0, env));
}).add;

// SynthDef(\res, { | out=0 gate=1 freq fltBus |
// 	var aEnv, osc, flt;
// 	aEnv = EnvGen.kr(Env.perc(0, 0.7), gate, doneAction: 2);
// 	osc = Mix([Saw.ar(freq), Pulse.ar(freq / 2, 0.5)]);
// 	flt = RLPF.ar(osc, In.kr(fltBus, 1), 0.1, aEnv);
// 	Out.ar(out, Pan2.ar(flt, 0));
// }).add;

// SynthDef(\resControl, { | fltBus |
// 	ReplaceOut.kr(fltBus, LFNoise1.kr(0.3, 1000, 1500));
// }).add;

SynthDef(\kwpoly, { | out=0 gate=1 freq amp=1 |
	var aEnv, fEnv, osc1, osc2, flt;
	aEnv = EnvGen.kr(Env.asr(0.2, 1, 0.1), gate, doneAction: 2);
	fEnv = EnvGen.kr(Env.asr(7, 1, 0.2), levelScale: 12000);
	osc1 = Pulse.ar(freq * [1, 1.007], LFCub.kr(2, 0, 0.3, 0.5));
	osc2 = Pulse.ar(freq / 2, 0.3);
	flt = RLPF.ar(osc1 + osc2, fEnv, 0.9, aEnv);
	Out.ar(out, flt * amp);
}).add;

// SynthDef(\solo, { | out=0 gate freq |
// 	var aEnv, fEnv, osc, flt;
// 	aEnv = EnvGen.kr(Env.asr(0.2, 1, 2), gate);
// 	fEnv = EnvGen.kr(Env.adsr(0.4, 1.2, 0.7, 2), gate, 800);
// 	osc = Saw.ar(Lag.kr(freq, 0.1) * [1, 1.005]);
// 	flt = LPF.ar(osc, fEnv + 600, aEnv);
// 	Out.ar(out, flt);
// }).add;

// SynthDef(\soloControl, { | gateBus freqBus gate=1 freq |
// 	ReplaceOut.kr(gateBus, gate);
// 	ReplaceOut.kr(freqBus, freq);
// 	EnvGen.kr(Env.asr(0,1,0), gate, doneAction: 2);
// }).add;

SynthDef(\kwstring, { | out=0 gate=1 freq=1000 |
	var aEnv, osc, flt;
	aEnv = EnvGen.kr(Env.asr(0.2, 1, 0.5), gate, doneAction: 2);
	osc = Saw.ar([LFCub.kr(0.3, Rand(0, 1), freq * 0.003, freq), freq, LFCub.kr(0.7, Rand(0, 1), freq * 0.001, freq)]);
	flt = LPF.ar(osc, 1500, aEnv);
	Out.ar(out, flt);
}).add;

SynthDef(\kwbass, { | out=0 gate=1 freq amp=1|
	var aEnv, fEnv, osc, flt;
	aEnv = EnvGen.kr(Env.asr(0, 1, 1), gate, doneAction: 2);
	fEnv = EnvGen.kr(Env.perc(0, 3), levelScale: 6000);
	osc = Mix([Saw.ar(freq * [1, 1.005]), Pulse.ar(freq / 2, 0.5)]);
	flt = LPF.ar(osc, fEnv + 100, aEnv);
	Out.ar(out, flt * amp);
}).add;

SynthDef(\kwsaw, { | out=0 freq t_trig=1 amp=1|
	var trig, div,  oct, reps, env, osc, flt;
	// oct = [0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3];
	// trig = Impulse.kr(EnvGen.kr(Env([1.75, 120], [30], 6.5)), doneAction:2);
	trig = t_trig;
	// div = PulseDivider.kr(trig, 16, 16);
	// reps = PulseCount.kr(div);
	env = EnvGen.kr(Env.perc(0, 3), trig, amp * 2, doneAction:2);
	// freq = (Stepper.kr(trig, div, 42, 74, 2, 42) + (12 * Select.kr(reps, oct))).midicps;
	osc = Saw.ar(freq * [1, 1.01], env);
	flt = RLPF.ar(osc, freq * 2, 0.7);
	Out.ar(out, flt);
}).add;
)