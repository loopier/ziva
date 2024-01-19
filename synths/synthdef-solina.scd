(
SynthDef(\solina, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1);
    var atk = \atk.kr(0.5);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(1.0);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var spread = \spread.kr(0.2);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb, del1, del2, del3;
    sig = Pulse.ar(freq * 2 + LFNoise2.kr(0.1).range(-0.1,0.1), LFNoise1.ar(Rand(0.1,0.2)).range(0.05, 0.2));
    sig = sig + Pulse.ar(freq * 2 + LFNoise2.kr(0.1).range(-0.1,0.1), LFNoise1.ar(Rand(0.1,0.2)).range(0.05, 0.2));
	sig = LPF.ar(sig, Rand(5999, 6001));
	sig = HPF.ar(sig, Rand(199,201));
	del1 = sig + DelayL.ar(sig,  0.04, SinOsc.ar(Rand(0.1,0.3)).range(0.024,0.035), mul: 0.6);
	del2 = sig + DelayL.ar(sig, 0.04, SinOsc.ar(Rand(0.4,0.6)).range(0.024,0.035), mul: 0.6);
	del3 = sig + DelayL.ar(sig, 0.04, SinOsc.ar(Rand(0.7,0.9)).range(0.024,0.035), mul: 0.6);
	sig = Splay.ar([del1, sig, del2, del3], spread);
    Out.ar(out, Pan2.ar(sig * env * amp/4 * (-12.dbamp), pan));
}).add;

)
