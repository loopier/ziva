// original code by Patrick Borgeat
(
SynthDef(\moog, {
	var gate = \gate.kr(1);
	var out = \out.kr(0);
	var ampLfoFreq = \ampLfoFreq.kr(0.1);
	var filterMode = \filterMode.kr(0);
	var dataUpdateId = \dataUpdateId.kr(0);

	var freqSource = \freqSource.kr(0);
	var freqConst = \freq.kr(200);
	var freqLfoFreq = \freqLfoFreq.kr(0.1);
	var freqRandFreq = \freqRandFreq.kr(0.3);
	var freqLower = \freqLower.kr(200);
    var freqUpper = \freqUpper.kr(400);

	var resonSource = \resonSource.kr(0);
	var resonConst = \resonConst.kr(0.3);
	var resonLfoFreq = \resonLfoFreq.kr(0.12);
	var resonRandFreq = \resonRandFreq.kr(0.35);
	var resonLower = \resonLower.kr(0);
	var resonUpper = \resonUpper.kr(0.3);

	var filterSource = \filterSource.kr(0);
	var filterConst = \filterConst.kr(1200);
	var filterLfoFreq = \filterLfoFreq.kr(0.15);
    var filterRandFreq = \filterRandFreq.kr(0.325);
	var filterLower = \filterLower.kr(100);
	var filterUpper = \filterUpper.kr(200);

	var sig;
	var atk = \atk.kr(0.01);
	var sus = \sus.kr(1);
	var rel = \rel.kr(1);
	var env = Linen.kr(gate, atk, sus, rel, doneAction: 2);
	var amp = SinOsc.kr(ampLfoFreq).range(0.5, 1);

	var filteredFreqLower = Lag2.kr(freqLower, 1);
	var filteredFreqUpper = Lag2.kr(freqUpper, 1);
	var filteredResonLower = Lag2.kr(resonLower, 1);
	var filteredResonUpper = Lag2.kr(resonUpper, 1);
	var filteredFilterLower = Lag2.kr(filterLower, 1);
	var filteredFilterUpper = Lag2.kr(filterUpper, 1);

	var freqLfo = SinOsc.kr(freqLfoFreq).range(filteredFreqLower, filteredFreqUpper);
	var freqRand = LFNoise2.kr(freqRandFreq).range(filteredFreqLower, filteredFreqUpper);
	var freq = Lag2.kr(Select.kr(freqSource, [freqConst, freqLfo, freqRand]), 0.05);

	var resonLfo = SinOsc.kr(resonLfoFreq).range(filteredResonLower, filteredResonUpper);
	var resonRand = LFNoise2.kr(resonRandFreq).range(filteredResonLower, filteredResonUpper);
	var reson = Lag2.kr(Select.kr(resonSource, [resonConst, resonLfo, resonRand]), 0.05);

	var filterLfo = SinOsc.kr(filterLfoFreq).range(filteredFilterLower, filteredFilterUpper);
	var filterRand = LFNoise2.kr(filterRandFreq).range(filteredFilterLower, filteredFilterUpper);
	var filter = Lag2.kr(Select.kr(filterSource, [filterConst, filterLfo, filterRand]), 0.05);

	sig = BMoog.ar(
		LFSaw.ar([freq * 0.99, freq * 1.01], 0, 0.1) ! 2,
		filter + SinOsc.kr(0.25).range(-10, 10),
		reson,
		filterMode);    // filter mode - lowpass
	sig = sig * amp;
	sig = sig.tanh;
	sig = (CombN.ar(sig, 0.4, [0.4,0.35],2) * 0.4) + (sig * 0.5);
	sig = sig.tanh;

	CtxDataUpdate.kr(dataUpdateId, 24, [freq, filter, reson, amp]);

	Out.ar(out, sig * env);
}).add;
)
