Psynth {
	*new { |name ... pairs|
		^Pbind(\type, \ziva_synth, \instrument, name, *pairs);
	}
}

Psample {
	*new { |sound, ch=2 ... pairs|
		^Pbind(\type, \sample, \sound, sound, \ch, ch, *pairs);
	}
}

Pmidi {
	*new { |midiout, ch=0|
		^Pbind(\type, \midi, \midiout, midiout, \chan, ch);
	}

	// cc { |num, val|
	// 	^Pchain(Pbind(\midi))
	// }
}

Pavldrums {
	*new { |midiout, ch=0|
		^Pbind(\type, \midi, \midiout, midiout, \chan, ch, \octave, 3, \amp, Pwhite(0.7));
	}
}