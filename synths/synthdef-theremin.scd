// (
// SynthDef(\theremin, {
// 	var mod = \mod.kr(7);
// 	var detune = \detune.kr(0);
// 	var amp = \amp.kr(0.8);
// 	var freq = \freq.kr(440);
// 	var f, a, z;
// 	// f = MouseY.kr(4000, 200, 'exponential', 0.8) + detune;
// 	// a = SinOsc.ar(f + (f * SinOsc.ar(mod,0,0.02)), mul: MouseX.kr(0, 0.9));
// 	f = freq + detune;
// 	a = SinOsc.ar(f + (f * SinOsc.ar(mod,0,0.02)), mul: amp.lag(1));
// 	z = Mix.ar(a);
// 	Out.ar(0, z) + Out.ar(1, z)
// }).add;
// )

// a = Synth(\theremin)

(
SynthDef(\theremin, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1);// * (-10.dbamp);
    var atk = \atk.kr(0.5);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var mod = \mod.kr(7);
	var detune = \detune.kr(0);
    var env = EnvGen.ar(
        Env.asr(atk, sus, rel),
        gate: gate,
        timeScale: \sustain.kr(1),
        doneAction: 2
	);
    var sig, fb, f, a, z;
	f = freq + detune;
	a = SinOsc.ar(f + (f * SinOsc.ar(mod,0,0.02)), mul: amp.lag(1));
	z = Mix.ar(a);
	sig = a;
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)
