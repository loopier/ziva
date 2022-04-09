(
SynthDef(\karplus2, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-0.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(1.0) * Select.kr(\bell.kr(0), [1,-1]); // negative emphasize odd partials
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var in = BrownNoise.ar(1);
	var trig = gate;
	// var coef = \coef.kr(0.1);
	var coef = Select.kr(\voice.kr(0), [0,0.1, 0.5,0.91,-0.05]);
	var rq = \rq.kr(1.0);
	var rate = \wah.kr(0.3);
    // var env = EnvGen.ar(
    //     Env.perc(atk, rel),
    //     gate: gate,
    //     doneAction: 2
	// );
    var env = Linen.kr(gate, 0.05, 1, 0.4, 2);
    var sig;
    sig = Pluck.ar(in, trig, freq.reciprocal, freq.reciprocal, dec, coef);
	// sig = RLPF.ar(sig, cutoff * env, rq, 1);
	sig = RLPF.ar(sig, LinExp.kr(LFNoise1.kr(rate), -1, 1, 200, 8000), rq);
    Out.ar(out, Pan2.ar(sig * amp, pan));
}).add;

SynthDef(\karplus, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-0.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(1.0) * Select.kr(\bell.kr(0), [1,-1]); // negative emphasize odd partials
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	// var in = WhiteNoise.ar(1);
	var in = WhiteNoise.ar(1);
	var trig = gate;
	// var coef = \coef.kr(0.1);
	var coef = Select.kr(\voice.kr(0), [0,0.1, 0.5,0.91,-0.05]);
	var rq = \rq.kr(1.0);
	var rate = \wah.kr(0.3);
    // var env = EnvGen.ar(
    //     Env.perc(atk, rel),
    //     gate: gate,
    //     doneAction: 2
	// );
    var env = Linen.kr(gate, 0.05, 1, 0.4, 2);
    var sig;
    sig = Pluck.ar(in, trig, freq.reciprocal, freq.reciprocal, dec, coef);
	// sig = RLPF.ar(sig, cutoff * env, rq, 1);
	sig = RLPF.ar(sig, LinExp.kr(LFNoise1.kr(rate), -1, 1, 200, 8000), rq);
    Out.ar(out, Pan2.ar(sig * amp, pan));
}).add;
)
