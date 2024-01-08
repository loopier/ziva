// + Ndef {
// 	sound {|snd|
// 		if( Ziva.samples.includes(snd) ) {
// 			this.source = Pbind(\type, \sample, \sound, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
// 		} {
// 			this.source = Pbind(\type, \note, \instrument, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
// 		}
// 	}

// 	s { |snd| this.sound(snd) }

// 	midi {|ch|
// 		this.source = Pbind(\type, \midi, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
// 	}

// 	doesNotUnderstand { |selector, args|
// 		var pairs;
// 		if(this.source.isKindOf(Pbind)) {
// 			pairs = this.source.patternpairs.asDict.put(selector.asSymbol, args).asPairs;
// 			this.source = Pbind(*pairs);
// 		}
// 	}

// 	fx { |effects|
// 		var fxndef = Ndef(('fx_'++this.key).asSymbol);
// 		"Add FX to %: %".format(this.name, effects).postln;
// 		if( effects.isArray ) {
// 			effects.do{|effect, i|
// 				fxndef[i+1] = \filter -> (Ziva.fxDict[effect.asSymbol] ? effect); // second option is a function
// 			};
// 			// clear unused indices if needed
// 			if( effects.size < fxndef.sources.size ) {
// 				var diff = fxndef.sources.size - effects.size;
// 				diff.do{|i| fxndef[effects.size + i + 1] = nil };
// 			};

// 			fxndef.sources.do{|x| x.postcs};
// 		} {
// 			fxndef.sources.size.debug(fxndef.name);
// 			fxndef[fxndef.sources.size] = \filter -> Ziva.fxDict[effects.asSymbol];
// 		}
// 	}

// 	lfo { |func|
// 		this.source = func;
// 	}

// 	gain { |level|
// 		this.amp = level.min(1).explin(0.01, 1.0, 0, 1);
// 	}

// 	spread { | amt=0.0 |
// 		var pairs;
// 		if(this.source.isKindOf(Pbind)) {
// 			pairs = this.source.patternpairs.asDict.put(\pan, [amt.neg, amt]).asPairs;
// 			this.source = Pbind(*pairs);
// 		}
// 	}

// 	// functions
// 	freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
// 	reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
// 	gverb {| time, damp | ^{| in | HPF.ar(GVerb.ar(in, roomsize:this, revtime:time, damping:damp, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
// 	delay {| decay=0 | ^{| in | AllpassC.ar(in, 4, this, decay )}}
// 	fbdelay {| fb=0.8 |
// 		^{| in |
// 			var local;
// 			// read feedback , add to source
// 			local = LocalIn.ar(2) + in;
// 			// delay sound
// 			local = DelayN.ar(local, 4, this);
// 			// reverse channels to give ping pong effect, apply decay factor
// 			// LocalOut.ar(local.reverse * fb);
// 			LocalOut.ar(local * fb);
// 			local
// 		}
// 	}
// 	lpf {| res = 1 | ^{| in | RLPF.ar(in, this, res)}}
// 	hpf {| res = 1 | ^{| in | RHPF.ar(in, this, res)}}
// 	bpf {| res = 1 | ^{| in | BPF.ar(in, this, res)}}
// 	brf {| res = 1 | ^{| in | BRF.ar(in, this, res)}}
// 	vcf {| res=0.7, mul=1 |  ^{| in | MoogVCF.ar(in, this, res, mul: mul)} }
// 	tremolo {| depth=0.3 | ^{| in | in * SinOsc.ar(this, 0, depth, 0)}}
// 	vibrato {| depth=0.3 | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(this, 0, depth, 1))}}
// 	crush {^{| in | in.round(0.5 ** (this-1));}}
// 	compress {^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:this)}}
// 	limit {| dur=0.01 | ^{| in | Limiter(in, this, dur)}}
// 	fold {| max=1 | ^{| in | LeakDC.ar( in.fold(this, max) )}}

// 	// wet { | index, amt=1 | Ndef((this.key++'_fx')).set((\wet++index).asSymbol, amt) }
// 	wet { | index, amt=1 | this.set((\wet++index).asSymbol, amt) }

// 	delay {| decay=0 | ^{| in | AllpassC.ar(in, 4, this, decay )}}
// 	fbdelay {| fb=0.8 |
// 		^{| in |
// 			var local;
// 			// read feedback , add to source
// 			local = LocalIn.ar(2) + in;
// 			// delay sound
// 			local = DelayN.ar(local, 4, this);
// 			// reverse channels to give ping pong effect, apply decay factor
// 			// LocalOut.ar(local.reverse * fb);
// 			LocalOut.ar(local * fb);
// 			local
// 		}
// 	}
// }