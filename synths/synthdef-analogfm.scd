(
SynthDef(\analogfm, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var modfreq = \modfreq.kr(700);
	var modamt = \modamt.kr(5).min(8.99);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb, mod, amt;
	mod = SinOsc.ar(modfreq, 0, -1);
	amt = modamt + [LFNoise2.ar([0.1,0.101]).range(-0.01,0.01)];
	freq = freq + LFNoise2.ar([0.3,0.3]).range(-0.001,0.001) * (2**(mod*amt));
	// freq = freq * [1,1.01];
	sig = LFTri.ar(freq,mul: 0.25) + SinOsc.ar(freq,mul: 0.75);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)