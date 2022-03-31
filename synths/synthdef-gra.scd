(
SynthDef(\gra, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1);// * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(0.0);
    var gate = \gate.kr(1);
	var buf = \buf.kr(0);
	// var pointer = Phasor.ar(gate, SampleDur.ir / BufDur.ir(buf) * XLine.kr(1,0.25,20)).poll;
	// var pointer = \pointer.kr(0) * SampleDur.ir / BufDur.ir(buf);
	var pointer = \pointer.kr(0);
	var freqscale = \freqscale.kr(2); // 0.5 = octave down; 2.0 = octave up
	var windowsize = \window.kr(0.2);
	// signal to use for grain envelope.
	// -1 uses built-in Hanning envelope.
	var envbufnum = \envbufnum.kr(-1);
	var overlaps = \overlaps.kr(8); // overlapping windows.
	var windowrandratio = \winrand.kr(0.0); // 0 no random; 1 too random
	var interp = \interp.kr(1); // pitchshift interpolation: 1=no; 2=lin; 4=cub
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb;

    sig = Warp1.ar(1, buf, pointer, freqscale, windowsize, envbufnum, overlaps, windowrandratio, interp);

    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)

// Ziva.boot;
// Ziva.loadSamples("~/loopier/samples");

// x = Synth(\gra, [buf:Ziva.samplesDict.at(\delia)[0]])
// x.free
// x.set(\pointer, 0.05);
// x.set(\envbuf, 0.1);

// a = Psynth(\gra, \buf, Ziva.samplesDict.at(\delia)[0]);
// (
// [
// 	a.fff.faster.legato.buf(Ziva.samplesDict.at(\jimmy)[(0..7).chooseN(8)].pseq).pointer((..10).chooseN(8).pseq/10).window((..10).chooseN(8).pseq/10).overlaps((1..10).chooseN(8).pseq),
// 	// a.fff.fast.stacc.pointer((..10).chooseN(4).pseq/10).window((..10).chooseN(4).pseq/10).overlaps((1..10).chooseN(4).pseq).freqscale(1/2),
// 	// a.fff.faster.pizz.rel(1).n((..7).prand).pointer((..10).chooseN(4).pseq/10).window((..10).chooseN(4).pseq/10).overlaps((1..10).chooseN(4).pseq).freqscale(4),
// ].ziva;
// )
