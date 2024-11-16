(
SynthDef(\brass, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
	// amp
    var amp = \amp.kr(1);
    var atk = \atk.kr(0.05);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(0.354);
	// filter
	var famt = \famt.kr(0.5); // filter amount
    var fatk = atk;
    var fdec = fatk * 3;
    var fsus = \fsus.kr(0.5);
    var frel = rel / 2;
    var gate = \gate.kr(1);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel,curve:[-4,0,0,4]),
        gate: gate,
        doneAction: 2
	);
    var fenv = EnvGen.ar(
        Env.adsr(fatk, fdec, fsus, frel),
        gate: gate,
        doneAction: 2
	);
	var flfo = LFNoise2.kr(15).range(0.5, 1.5);
    var sig;
	var blip = EnvGen.ar(Env.perc(0.01, 0.1), gate).linlin(0,1,1,1.2);
	// var blip = 1;

    sig = Saw.ar(freq * [1.01, blip]);
	sig = Mix.ar(sig);
	sig = LPF.ar(sig, freq * amp * 10, fenv * flfo);
	sig = FreeVerb.ar(sig, 0.33, 0.5);

	amp = amp * (12.dbamp);

    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)