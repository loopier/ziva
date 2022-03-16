(
SynthDef(\plaits, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1) * (-10.dbamp);
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
	var engine = \engine.kr(0);
	var harm = \harm.kr(0.1);
	var timbre = \timbre.kr(0.5);
	var morph = \morph.kr(0.5);
	// var lvl = \lvl.kr(0);
	var lvl = gate;
	var fmamt = \fmamt.kr(0);
	var timbamt = \timbamt.kr(0);
	var morphamt = \morphamt.kr(0);
	var lpgdec = \lpgdec.kr(0.5);
	var lpg = \lpg.kr(0.5);
	var sig, fb;
    sig = MiPlaits.ar(
		pitch: freq.cpsmidi,
		engine: engine,
		harm: harm,
		timbre: timbre,
		morph: morph,
		trigger: gate,
		level: lvl,
		fm_mod: fmamt,
		timb_mod: timbamt,
		moph_mod: morphamt,
		decay: lpgdec,
		lpg_colour: lpg,
	);
	// sig = Mix.ar(sig);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
},
	variants: (
		numbers: [engine: 7, harm: 0.6], // timbre: filter - morph: number
		letters: [engine: 7, harm: 0.7], // timbre: filter - morph: letter
		radio: [engine: 7, harm: 0.8], // timbre: filter - morph: letter
		synth: [engine: 7, harm: 0.8], // timbre: filter - morph: synth
		// bla: [engine: 6, harm: 0.5, morph: 0.5], // timbre: filter - morph: number
	)
).add;
)

// (
// Pdef(\master, Ppar([
// 	Pbind(\instrument, 'plaits', \amp, 1, \dur, 1/2, \engine, 6, \morph, Pwhite(), \degree, Pbrown(0,7,1))
// ])).play.quant_(1);
// )
// Pdef.removeAll
// TempoClock.default.tempo =1
