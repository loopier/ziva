SynthDef.new(\kick_808, {
    arg out=0;
	var midinote=\pitch.kr(36), decay=\decay.kr(1.5), amp=2;
    var freq, fenv, env, trienv, sig, sub, punch, pfenv;
	var trigger = \trig.kr(1); // Convert trig to a trigger signal
	var att=\attack.kr(0.1)/100;
	var tri=\tri.kr(0.6); //0-100
    freq = midinote.midicps;

    // Use Env.perc for sharper attack and proper release

    env = EnvGen.ar(Env.perc(att, decay, 1, -4), trigger, doneAction: 2);
    trienv = EnvGen.ar(Env.perc(att, decay * 0.8, tri, -4), trigger);

    // Use EnvGen.ar for frequency envelopes to ensure proper reset
    fenv = EnvGen.ar(Env([freq * 7, freq * 1.35, freq], [0.05, 0.6], -14), trigger);
    pfenv = EnvGen.ar(Env([freq * 7, freq * 1.35, freq], [0.03, 0.6], -10), trigger);

    // Reset oscillator phases
    sig = SinOsc.ar(fenv, phase: trigger * pi/2) * env;
    sub = LFTri.ar(fenv, iphase: trigger) * trienv * 0.05;
    punch = SinOsc.ar(pfenv, phase: trigger * pi/2) * env * 2;

	punch = HPF.ar(punch, 350);
    sig = (sig + sub + punch);
    sig = LeakDC.ar(sig); // Remove any DC offset
    sig = Limiter.ar(sig, 0.5) * amp;
    sig = Pan2.ar(sig);
	Out.ar(out, sig * \levels.kr(1));
}).add;

SynthDef(\bd, {
    arg midinote=36, decay=30, amp=2, gate=0;
    var freq, fenv, env, trienv, sig, sub, punch, pfenv;
    freq = midinote.midicps / 4; // Convert MIDI note to frequency; down 2 octaves
    env = EnvGen.kr(Env.new([0.11, 1, 0], [0, decay], -225), doneAction: 2);
    trienv = EnvGen.kr(Env.new([0.11, 0.6, 0], [0, decay], -230), doneAction: 0);
    fenv = Env([freq * 7, freq * 1.35, freq], [0.05, 0.6], -14).kr;
    pfenv = Env([freq * 7, freq * 1.35, freq], [0.03, 0.6], -10).kr;
    sig = SinOsc.ar(fenv, pi/2) * env;
    sub = LFTri.ar(fenv, pi/2) * trienv * 0.05;
    punch = SinOsc.ar(pfenv, pi/2) * env * 2;
    punch = HPF.ar(punch, 350);
	sig = (sig + sub + (punch)) * 2.5;
    sig = Limiter.ar(sig, 0.5) * amp;
    sig = Pan2.ar(sig, 0);
    Out.ar(0, sig);
}).add;

SynthDef.new(\sn, {
	arg amp=2, tone=340, tone2=189, snappy=0.3, gate=0, amp2=1;
	var noiseEnv, atkEnv, sig, noise, osc1, osc2, sum;
	noiseEnv = EnvGen.kr(Env.perc(0.001, 4.2, 1, -115), doneAction:2);
	atkEnv = EnvGen.kr(Env.perc(0.001, 0.8,curve:-95), doneAction:0);
	noise = WhiteNoise.ar;
	noise = HPF.ar(noise, 1800);
	noise = LPF.ar(noise, 8850);
	noise = noise * noiseEnv * snappy;
	osc1 = SinOsc.ar(tone2, pi/2) * 0.6;
	osc2 = SinOsc.ar(tone, pi/2) * 0.7;
	sum = (osc1+osc2) * atkEnv * amp2;
	sig = Pan2.ar((noise + sum) * amp * 2.5, 0);
	sig = HPF.ar(sig, 340);
	Out.ar(0, sig);
}).add;

SynthDef.new(\cpRaw, {
	arg amp=1, gate=0;
	var atkenv, atk, decay, sum, denv;
	atkenv = EnvGen.kr(Env.new([0.5,1,0],[0, 0.3], -160), doneAction:2);
	denv = EnvGen.kr(Env.dadsr(0.026, 0, 6, 0, 1, 1, curve:-157), doneAction:0);
	atk = WhiteNoise.ar * atkenv * 1.4;
	decay = WhiteNoise.ar * denv;
	sum = atk + decay * amp;
	sum = HPF.ar(sum, 500);
	sum = BPF.ar(sum, 1062, 0.5);
	Out.ar(0, Pan2.ar(sum * 1.5, 0));
}).add;

SynthDef.new(\LT, {
	arg amp=1, freq=80;
	var sig, fenv, env, pnoise, penv;
	env = EnvGen.kr(Env.new([0.4, 1, 0], [0, 20], -250),doneAction:2);
	penv = EnvGen.kr(Env.new([0.6, 1, 0], [0, 30], -225),doneAction:2);
	fenv = Env([freq*1.25, freq*1.125, freq], [0.1, 0.5], -4).kr;
	sig = SinOsc.ar(fenv, pi/2) * env;
	sig = Pan2.ar(sig * amp * 3, 0);
	Out.ar(0, sig);
}).add;

SynthDef.new(\rimshot, {
	arg amp=1;
	var fenv, env, sig, punch, tri1, tri2;
	env = EnvGen.kr(Env.new([1, 1, 0], [0.00272, 0.07], -42), doneAction:2);
	tri1 = LFTri.ar(1667 * 1.1, 1) * env;
	tri2 = LFPulse.ar(455 * 1.1, width:0.8) * env;
	punch = WhiteNoise.ar * env * 0.46;
	sig = tri1 + tri2 + punch;
	sig = BPeakEQ.ar(sig, 464, 0.44, 8);
	sig = HPF.ar(sig, 315);
	sig = LPF.ar(sig, 7300);
	sig = Pan2.ar(sig * amp, 0);
	Out.ar(0, sig);
}).add;

SynthDef.new(\claves, {
	arg amp=1;
	var  env, sig;
	env = EnvGen.kr(Env.new([1, 1, 0], [0, 0.1], -20), doneAction:2);
	sig = SinOsc.ar(2500, pi/2) * env * amp;
	sig = Pan2.ar(sig, 0);
	Out.ar(0, sig);
}).add;

SynthDef.new(\maracas, {
	arg amp=1, gate=1;
	var env, sig;
	env = EnvGen.kr(Env.new([0.3, 1, 0], [0.027, 0.07], -250), gate, doneAction:2);
	sig = WhiteNoise.ar * env * amp;
	sig = HPF.ar(sig, 5500);
	sig = Pan2.ar(sig, 0);
	Out.ar(0, sig);
}).add;

SynthDef.new(\cowbell, {
	arg amp=1;
	var sig, pul1, pul2, env, atk, atkenv, datk;
	atkenv = EnvGen.kr(Env.perc(0, 1, 1, -215),doneAction:0);
	env = EnvGen.kr(Env.perc(0.01, 9.5, 1, -90),doneAction:2);
	pul1 = LFPulse.ar(811.16);
	pul2 = LFPulse.ar(538.75);
	atk = (pul1 + pul2) * atkenv * 6;
	datk = (pul1 + pul2) * env;
	sig = (atk + datk) * amp;
	sig = HPF.ar(sig, 250);
	sig = LPF.ar(sig, 4500);
	sig = Pan2.ar(sig, 0);
	Out.ar(0, sig);
}).add;

SynthDef.new(\hat, {
	arg decay=0.42, amp=1, pan=0;
	var sig, sighi,siglow, sum, env, osc1, osc2, osc3, osc4, osc5, osc6;
	env = EnvGen.kr(Env.perc(0.005, decay, 1, -30),doneAction:2);
	osc1 = LFPulse.ar(203.52);
	osc2 = LFPulse.ar(366.31);
	osc3 = LFPulse.ar(301.77);
	osc4 = LFPulse.ar(518.19);
	osc5 = LFPulse.ar(811.16);
	osc6 = LFPulse.ar(538.75);
	sighi = (osc1 + osc2 + osc3 + osc4 + osc5 + osc6);
    siglow = (osc1 + osc2 + osc3 + osc4 + osc5 + osc6);
    sighi = BPF.ar(sighi, 8900, 1);
    sighi = HPF.ar(sighi, 9000);
    siglow = BBandPass.ar(siglow, 8900, 0.8);
    siglow = BHiPass.ar(siglow, 9000, 0.3);
    sig = BPeakEQ.ar((siglow+sighi), 9700, 0.8, 0.7);
    sig = sig * env * amp;
    sig = Pan2.ar(sig, pan);
    Out.ar(0, sig);
}).add;

SynthDef.new(\openhihat, {
	arg decay=0.5, amp=4, pan=0;
	var sig, siga, sigb, env1, env2, osc1, osc2, osc3, osc4, osc5, osc6, sum;
	env1 = EnvGen.kr(Env.perc(0.1, decay, curve:-3), doneAction:2);
	env2 = EnvGen.kr(Env.new([0, 1, 0], [0, decay*5], curve:-150), doneAction:0);
	osc1 = LFPulse.ar(203.52) * 0.6;
	osc2 = LFPulse.ar(366.31) * 0.6;
	osc3 = LFPulse.ar(301.77) * 0.6;
	osc4 = LFPulse.ar(518.19) * 0.6;
	osc5 = LFPulse.ar(811.16) * 0.6;
	osc6 = LFPulse.ar(538.75) * 0.6;
	sig = osc1 + osc2 + osc3 + osc4 + osc5 +osc6;
	sig = BLowShelf.ar(sig, 990, 2, -3);
	sig = BPF.ar(sig, 7700);
	sig = BPeakEQ.ar(sig, 7200, 0.5, 5);
	sig = BHiPass4.ar(sig, 8100, 0.7);
	sig = BHiShelf.ar(sig, 9400, 1, 5);
	siga = sig * env1 * 0.6;
	sigb = sig * env2;
	sum = siga + sigb;
	sum = LPF.ar(sum, 4000);
	sum = Pan2.ar(sum, 0);
	sum = sum * amp * 2;
	Out.ar(0, sum);
}).add;

SynthDef.new(\cymbal, {
	arg decay=2, amp=20, pan=0, gate=0, tone=0.002;
	var sig, sig1,sig2, sig2a, sig2b, sig3, env1, env2, env2b, env3, osc1, osc2, osc3, osc4, osc5, osc6, sum;
	env1 = EnvGen.kr(Env.perc(0.3, decay, curve:-3), doneAction:2);
	env2 = EnvGen.kr(Env.new([0, 0.6, 0], [0.1, decay*0.7], -5), doneAction:0);
	env2b = EnvGen.kr(Env.new([0, 0.3, 0], [0.1, decay*20], -120), doneAction:0);
	env3 = EnvGen.kr(Env.new([0, 1, 0], [0, decay*5], curve:-150), doneAction:0);
	osc1 = LFPulse.ar(203.52) * 0.6;
	osc2 = LFPulse.ar(366.31) * 0.6;
	osc3 = LFPulse.ar(301.77) * 0.6;
	osc4 = LFPulse.ar(518.19) * 0.6;
	osc5 = LFPulse.ar(811.16) * 0.6;
	osc6 = LFPulse.ar(538.75) * 0.6;
	sig = osc1 + osc2 + osc3 + osc4 + osc5 +osc6;
	sig1 = BLowShelf.ar(sig, 2000, 1, 5);
	sig1 = BPF.ar(sig1, 3000);
	sig1 = BPeakEQ.ar(sig1, 2400, 0.5, 5);
	sig1 = BHiPass.ar(sig1, 1550, 0.7);
	sig1 = LPF.ar(sig1, 3000);
	sig1 = BLowShelf.ar(sig1, 1000, 1, 0);
	sig1 = sig1 * env1 * tone;
	sig2 = BLowShelf.ar(sig, 990, 2, -5);
	sig2 = BPF.ar(sig2, 7400);
	sig2 = BPeakEQ.ar(sig2, 7200, 0.5, 5);
	sig2 = BHiPass4.ar(sig2, 6800, 0.7);
	sig2 = BHiShelf.ar(sig2, 10000, 1, -4);
	sig2a = sig2 * env2 * 0.3;
	sig2b = sig2 * env2b * 0.6;
	sig3 = BLowShelf.ar(sig, 990, 2, -15);
	sig3 = BPF.ar(sig3, 6500);
	sig3 = BPeakEQ.ar(sig3, 7400, 0.35, 10);
	sig3 = BHiPass4.ar(sig3, 10500, 0.8, 2);
	sig3 = sig3 * env3;
	sum = sig1 + sig2a + sig2b + sig3;
	sum = LPF.ar(sum, 4000);
	sum = Pan2.ar(sum, 0);
	sum = sum * amp;
	Out.ar(0, sum);
}).add;