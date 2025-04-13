+ Number {

	!! { | repeats | ^Pseq([this], repeats) }
	motif { | degs = #[0,2,4,7] |
        var dur = [1, 1/4, 1/2].choosen(this);
        var deg = degs.choosen(this);
        ^(dur: Pseq(dur,inf), degree: Pseq(deg,inf));
	}

	// utils
	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
	white { | max=1 | ^Pwhite(this, max) }
	chop { | chunks=16 | ^(..chunks).choosen(this) / chunks }

	seed { thisThread.randSeed = this }
    unison { | spread=0.01 | ^Array.interpolation(this, spread.neg, spread) }

	// array
	geom{ |start=0, grow=1| ^Array.geom(this, start, grow) }
	series { |start=0, step=1| ^Array.series(this, start, step) }
	ser { |start=0, step=1| ^this.series(this, start, step) }
	interp { |start=0.0, end=1.0| ^Array.interpolation(this, start, end) }
	rnd { | min=0, max=1.0 | ^Array.rand(this, min, max) }
	rnd2 { | value=0 | ^Array.rand2(this, value) }
	linrnad { | min=0, max=1.0 | ^Array.linrand(this, min, max) }
	exprand { | min=0, max=1.0 | ^Array.exprand(this, min, max) }
	fib { | start=0, step=1 | ^Array.vib(this, start, step) }

	// after Mercury's total-serialism
	arp { |notesPerOctave=7| ^([0,2,4].dup(this) + ((..this-1) * notesPerOctave)).flat} // this = octave
	spread { |low=0.0, hi=0.1| ^interp(this, low, hi) }
	spread2 { |value=0.1| ^interp(this, value.neg, value) }
	dice { |sides=6| ^(1..sides).choosen(this) }
	twelveTone{ ^(..12).scramble }

	// funcs
	line	{ | end=1.0, dur=10 | ^{Line.ar(this, end, dur)}}
	xline	{ | end=1.0, dur=10 | ^{XLine.ar(this, end, dur)}}
	sine	{ | min=(-1), max=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) }}
	tri		{ | min=(-1), max=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) }}
	varsaw	{ | min=(-1), max=1, width=0.5| ^{VarSaw.ar(this,width: width).range(min,max)} }
	saw		{ | min=(-1), max=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) }}
	pulse	{ | min=(-1), max=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) }}
	noise0	{ | min=(-1), max=1 | ^{LFNoise0.ar(this).range(min,max) }}
	noise1	{ | min=(-1), max=1 | ^{LFNoise1.ar(this).range(min,max) }}
	noise2	{ | min=(-1), max=1 | ^{LFNoise2.ar(this).range(min,max) }}
	// envelopes env
	adsr { | dec=0.3, sus=0.5, rel=1, gate, doneAction=0 | ^{ Env.adsr(this, dec, sus, rel).ar(doneAction, gate) } }

	// fx
	gain { ^{| in | in * this } }
	// amp { ^{| in | in * this} }
	freeverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	// reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
	gverb {| room | ^{| in | HPF.ar(GVerb.ar(in, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
	delay {| decay=0 | ^{| in | AllpassC.ar(in, min(this,4), this, decay)}}
	swdelay {| fb=0.7, dry=1, wet=1 | ^{|in| SwitchDelay.ar(in, dry, wet, this, fb )}}
	fbdelay {| fb=0.8 |
		^{| in |
			var local;
			// read feedback , add to source
			local = LocalIn.ar(2) + in;
			// delay sound
			local = DelayN.ar(local, max(this, 0.2), this);
			// reverse channels to give ping pong effect, apply decay factor
			// LocalOut.ar(local.reverse * fb);
			LocalOut.ar(local * fb);
			local
		}
	}
	lpf {| res = 1 | ^{| in | RLPF.ar(in, max(this,1), res)}}
	hpf {| res = 1 | ^{| in | RHPF.ar(in, max(this,1), res)}}
	bpf {| res = 1 | ^{| in | BPF.ar(in, max(this,1), res)}}
	brf {| res = 1 | ^{| in | BRF.ar(in, max(this,1), res)}}
	vcf { | res=0.7, mul=1 |  ^{| in | MoogVCF.ar(in, this, res, mul: mul)} }
	tremolo {| depth=0.3 | ^{| in | in * SinOsc.ar(this, 0, depth, 0)}}
	vibrato {| depth=0.3 | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(this, 0, depth, 1))}}
	distort {| post, spread | ^this.asFloat.distort(post, spread) }
	// techno {| x | ^{| in | RLPF.ar(in, SinOsc.ar(0.1).exprange(880,12000), 0.2)}}
	// technosaw {| x | ^{| in | RLPF.ar(in, LFSaw.ar(0.2).exprange(880,12000), 0.2)}}
	// cyberpunk {| x | ^{| in | Squiz.ar(in, 4.5, 5, 0.1)}}
	// bitcrush {| x | ^{| in | Latch.ar(in, Impulse.ar(11000*0.5)).round(0.5 ** 6.7)}}
	// antique {| x | ^{| in | LPF.ar(in, 1700) + Dust.ar(7, 0.6)}}
	crush {^{| in | in.round(0.5 ** (this-1));}}
	// chorus2 {^{| in | Mix.fill(7, {
	// 	var maxdelaytime = rrand(0.005,0.02);
	// 	DelayC.ar(in, maxdelaytime, LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) );
	// })}}
	chorus { | min=1, max=20, spread=0.1, gain=1 |
		^{| in | Mix.fill(7, {
			// var maxdelaytime= rrand(0.005,0.02);
			Splay.ar(
				Array.fill(this,{
					var maxdelaytime= rrand(min * 0.001, max * 0.001);
					var del = DelayC.ar(in[0], maxdelaytime,LFNoise1.kr(Rand(0.1,0.6),0.25*maxdelaytime,0.75*maxdelaytime));
					// LinXFade2.ar(in, del, this.linlin(0.0,1.0, -1.0,1.0));
					del * gain;
				}),
				spread
			)
		})}
	}
	compress {^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:this)}}
	compress2{ | below=1, above=0.5 | ^{arg in; Compander.ar(in, in, this, below, above, 0.01, 0.01) }}
	limiter {| dur=0.01 | ^limit(dur)}
	limit {| dur=0.01 | ^{| in | Limiter.ar(in, this, dur)}}
}

+ Float {
	// freeverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	// reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
	// gverb {| room | ^{| in | HPF.ar(GVerb.ar(in, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}

	distor { |post=1, spread=0.2| ^{|in| Splay.ar((in * this.abs).distort, spread) * post }}
	// asymetric fold
	// \param neg	absolute value of the negative pole value, will be converted to negative
	afold { |neg=0.1, post=1|
		var posPre = this.max(0.01);
		var negPre = neg.max(0.01).neg;
		^{| in |
			LeakDC.ar(in.fold(negPre, posPre) * (1/negPre.abs + posPre)) * post
		}
	}
	// symetric fold
	wavefold { |post=1| ^{| in | in.fold2(this.max(0.01)) * (1/this.max(0.01))  * post }}
}

+ Integer {
	/// \brief	see `sound`
	s { |snd| ^this.sound(snd) }

	/// \brief	set the sound
	/// \param	snd:	can be either a synth or a sample
	sound {|snd|
		var key = (\track++this).asSymbol;
		Ziva.proxyspace.at(key).resume;
		Ziva.proxyspace.at(key).sound(snd);
		Ziva.proxyspace.at(key).mixer(this, 1);

		^Ziva.proxyspace.at(key);
	}

	reverb { "alo".debug }

	/// \brief	see `zyn`
	z { |ch| ^this.zyn(ch); }

	/// \brief	send MIDI events and OSC messages to ZynAddSubFx (OSC 127.0.0.1:4001)
	/// \param	ch:		MIDI channel.
	zyn { |ch|
		var key = (\track++this).asSymbol;
		^Ziva.proxyspace.at(key).zyn(ch);
	}

	/// \brief	connect to MIDI(0)
	/// \param	ch:		channel number [0..15]
	m { |ch| this.midi(ch) }

	midi {|ch|
		var key = (\track++this).asSymbol;
		^Ziva.proxyspace.at(key).midi(ch);
	}
	// controllers
	midifighter { | min=0.0, max=1.0, curve=\lin |
		// var key = (\midifighter++this).asSymbol.debug(this);
		var key = (\mf++this).asSymbol.debug(this);
		var nodeproxy = Ziva.proxyspace[key];
		// MIDIdef.cc(key, {|ccval| Ziva.proxyspace[key] = ccval.linlin(0,127,0.0,1.0)}, i, 0);
		MIDIdef.cc(
			key: key,
			func: {|ccval|
				if( curve == \lin ) {
					nodeproxy[0] = ccval.linlin(0,127,min,max);
				}{
					nodeproxy[0] = ccval.linexp(0,127, max(min, 0.001), max);
				}
			},
			ccNum: this,
			chan: 0,
		);
		Ziva.proxyspace[key].source.postcs;
		^Ziva.proxyspace[key]
	}

	mf { | min=0.0, max=1.0, curve=\lin | this.midifighter(min,max,curve) }
}