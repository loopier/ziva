(
SynthDef(\juno, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var spread = \spread.kr(1);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(1);
    var sus = \sus.kr(0.0);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
    var watk = \widthAtk.kr(0.01);
    var wdec = \widthDec.kr(0.1);
    var wsus = \widthSus.kr(0.0);
    var wrel = \widthRel.kr(0.5); // values lower than 0.5 result in clicks
    var cutoff = \cutoff.kr(15000);
    var res = \res.kr(1);
    var fatk = \filterAtk.kr(0.01);
    var fdec = \filterDec.kr(1.0);
    var fsus = \filterSus.kr(0.1);
    var frel = \filterRel.kr(0.1);
    var sub = \sub.kr(0).linlin(0.0,1.0,-1.0,1.0);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        // Env.perc(atk, dec),
        gate: gate,
        timeScale: \dur.kr(1),
        doneAction: 2
    );
    var widthEnv = EnvGen.ar(
        Env.adsr(watk, wdec, wsus, wrel),
        gate: gate,
        doneAction: 2
    ).linlin(0,1,0.5,0.1);
    var filterEnv = EnvGen.ar(
        Env.adsr(fatk, fdec, fsus, frel),
        gate: gate,
        doneAction: 2
    );
    var sig;
    sig = Pulse.ar(freq + [Rand(-1,1), Rand(-1,1)], widthEnv);
    sig = RLPF.ar(sig, cutoff * filterEnv, res);
    sig = XFade2.ar(HPF.ar(sig, 60), sig, sub);
    Out.ar(out, Pan2.ar(Splay.ar( sig, spread ) * env * amp, pan));
}).add;
)