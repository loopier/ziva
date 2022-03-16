(
SynthDef(\reese, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var voice = \voice.kr(0);
	var accelerate = \accelerate.kr(0);
	var detune = \detune.kr(0);
	var cutoff = \cutoff.kr(10000);
	var res = \res.kr(1);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb;

	var q1 = voice.linlin(0,2, 3.0, 0.5);
	var q2 = voice.linlin(0,2, 2.0, 7.0);
	freq = freq * XLine.ar(1, exp(accelerate), sus);
	sig = Splay.ar( VarSaw.ar(freq*[-1*detune/100+0.99,detune/100+1.01], [0.7,0.5], [0.01,0.02]), 0.2, 1, levelComp:false );
	sig = RLPF.ar(sig, freq*10, 1.0/q1);
	sig = sig.clip2(1.0/5.0)* 5.0;
	sig = 0.35*RLPF.ar(sig, freq*20, 1.0/q2);
	sig = RLPF.ar(sig, cutoff, res);

    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)
