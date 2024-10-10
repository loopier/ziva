(
SynthDef(\bass, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440) / 4;
    var amp = \amp.kr(1) * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
    var env = Env.adsr(atk, dec, sus, rel).ar(2, gate: gate);
    var sig;
    sig = Pulse.ar(freq);
	sig = sig + SinOsc.ar(freq/1);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).store;

SynthDef(\pbass, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440) / 4;
    var amp = \amp.kr(1);// * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
    var env = Env.adsr(atk, dec, sus, rel).ar(2, gate: gate);
    var sig;
    sig = Pulse.ar(freq + [Rand(-1.5,0),Rand(1.5)]);
    pan = pan + [-0.6,0.6];
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).store;

SynthDef(\deepbass, {
    var out = \out.kr(0);
    var pan = \pan.kr(0) + Rand(-0.1,0.1);
    var freq = \freq.kr(440) / 4 + Rand(-1,1);
    var amp = \amp.kr(1) * (-6.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(1);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var fb = \fb.kr(0);
	var distor = \distor.kr(0);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, sub;
    sig = SinOscFB.ar(freq, fb);
    sub = SinOscFB.ar(freq/2, fb);
	sig = ConstantGainDistortion.ar(sig, distor.max(0.0) * env);
	// sig = CrossoverDistortion.ar(sig, distor.max(1));
	sig = LPF.ar(sig, \cutoff.kr(15000));
	sig = Limiter.ar(sub + sig);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).store;
)