(
SynthDef(\crisp, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    // var freq = \freq.kr(440);
    var amp = \amp.kr(1);
    // var atk = \atk.kr(0.01);
    // var dec = \dec.kr(0.3);
    // var sus = \sus.kr(0.5);
    // var rel = \rel.kr(0.01);
    var gate = \gate.kr(1);
	var width = \width.kr(0.1);
    // var env = EnvGen.ar(
    //     Env.adsr(atk, dec, sus, rel),
    //     gate: gate,
    //     doneAction: 2
	// );
    var sig, fb;
    sig = WhiteNoise.ar(amp) * Trig.kr(gate, 0);
    Out.ar(out, Pan2.ar(sig * amp, pan));
}).add;
)
