(
SynthDef(\crisp, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var amp = \amp.kr(1);
    var gate = \t_gate.kr(1);
    var width = \width.kr(0.001);
    var sig, fb;
    sig = WhiteNoise.ar(amp) * Env.linen(0,width,0).kr(gate: gate, doneAction:2);
    Out.ar(out, Pan2.ar(sig * amp, pan));
}).add;
)
