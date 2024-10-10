(
SynthDef(\klank, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
	var amp = \amp.kr(1);
    var freq = \freq.ir(440);
    // var harm = \harm.ir(Array.series(8,1,2));
    var harm = \harm.ir(Array.series(8,1,3).sort.debug);
    var amps = \amps.ir(Array.fill(4, 0.05));
	var ring = \ring.ir(Array.rand(4, 1.0, 2.0));
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb;
    sig = Klank.ar(`[harm, amps, ring], env, freq, decayscale: rel + 0.025);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)