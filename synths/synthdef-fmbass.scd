(
SynthDef(\fmbass, {
	var lfofreq = \lfofreq.kr(1);
	var lfodepth = \lfodepth.kr(0);
	var freq = \freq.kr(440);// - (24.midicps); // 2 octaves down by default
    var tremolo =  (1 + (LFTri.kr(lfofreq) * lfodepth));
	var feedback = \feedback.kr(0);
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1);
	var dur = \dur.kr(1);
	var gate = \gate.kr(1);
	var attack = \atk.kr(0.001);
	var decay = \dec.kr(0.03);
	var sustain = \sus.kr(0.5);
	var release = \rel.kr(0.1);
	var cutoff = \cutoff.kr(10000);
	var res = \res.kr(1);
	// overall envelope
	var env = EnvGen.ar(Env.adsr(attack, decay, sustain, release), gate , timeScale:dur,
		doneAction:2);
	// operator output levels
	var amps = [1,0,0,0,0,0];
	// operator frequency ratios
	var ratios = [2,1,1,2,1,1];
	// operator frequency detuners
	var detunes = Array.fill(6, {|i| (\detune++(i+1)).asSymbol.kr(rand2(0.1))});
	// operator envelopes [[levels][rates]]
	var envs = [
		[[1,1,1], [0.01, 0.3, 1, 1]],
		[[1,1,1], [0.01, 0.3, 1, 1]],
		[[1,1,1], [0.01, 0.3, 1, 1]],
		[[1.5,0.1,1.5], [0.2 + rand2(0.05), 0.1 + rand2(0.05), 0.5 + rand2(0.1), 0.3 + rand2(0.1)]],
		[[1,1,1], [0.01, 0.3, 1, 1]],
		[[1,1,1], [0.01, 0.3, 1, 1]],
	];
	var ctls = Array.fill(6, { |i| [
		freq * tremolo * ratios[i] + detunes[i],
		0,
		EnvGen.kr(Env.new([0]++envs[i][0]++[0], envs[i][1]), timeScale:dur)
	]});
	// modulation matrix
	var mods = [
		[0.25 * feedback, 0.83, 0.9, 0.83, 0, 0],
		[0, 0.63 * feedback, 0, 0.53, 0, 0],
		[0, 0, 0, 0.53, 0, 0],
		[0, 0, 0, 0.7 * feedback, 0, 0],
		[0, 0, 0, 0, 0, 0],
		[0, 0, 0, 0, 0, 0],
	];

	var sound = FM7.ar(ctls, mods) * amps;
    sound = sound * amp * env;
	sound = Mix.ar(sound) * (-10.dbamp);
	sound = RLPF.ar(sound, cutoff, res);
	OffsetOut.ar(out, Pan2.ar(sound, pan));
}).store;
)
