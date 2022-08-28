// from SynthDEFaults:
// - A Collection of Traditional SynthDefs -
// under GNU GPL 3 as per SuperCollider license
// Organized by Zé Craum
// Examples at: http://sccode.org/1-5aE
(
// By Zé Craum
SynthDef(\organ, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1);//* (-10.dbamp);
    var atk = \atk.kr(0.001);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.4);
    var rel = \rel.kr(0.1);
    var gate = \gate.kr(1);
	var vrate = \vrate.kr(6);
	var vdepth = \vdepth.kr(0.02);
	var vdelay = \vdelay.kr(0.1);
	var vonset = \vonset.kr(0);
	var vrateVariation = \vrateVariation.kr(0.1);
	var vdepthVariation = \vdepthVariation.kr(0.1);
	var rq = \rq.kr(1);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
	// var env = EnvGen.ar(Env.linen(atk, sus, rel, 0.5), doneAction: Done.freeSelf);
    var sig, vibrato;
	vibrato = Vibrato.kr(DC.kr(freq), DC.kr(vrate), DC.kr(vdepth), DC.kr(vdelay), DC.kr(vonset), DC.kr(vrateVariation), DC.kr(vdepthVariation));
	sig = LFPulse.ar(freq, 0, 0.5, 0.2) + LFPulse.ar(freq + vibrato, 0, 0.18);
	sig = BLowPass4.ar(sig, ((28.midiratio) * (freq)) , rq) ;

	sig = sig * env;
	sig = sig * amp;
	sig = LeakDC.ar(sig);
	sig = sig * 4;
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)
