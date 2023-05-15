Psynth {
	*new { |name ... pairs|
		^Pbind(\type, \ziva_synth, \instrument, name, *pairs);
	}
}

Psample {
	*new { |sound ... pairs|
		if(Ziva.samplesDict.includesKey(sound)) {
			^Pbind(\type, \sample, \sound, sound, *pairs);
		} {
			"Sample '%' not found. Evaluate 'Ziva.samples' to see a list of available synths.".format(sound.asString).error;
		}
	}
}

// Prec {
// 	*new{ |name = \, length = 4, channels = 1|
// 		^P
// 	}
// }

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

Panimatron {
	var <>netAddr;
	// var <>actor;

	*new { | ip="127.0.0.1", port="56101" |
		^super.new.init(ip, port);
	}

	init { | ip, port |
		ip.debug("ip");
		port.debug("port");
		this.netAddr = NetAddr(ip, port);
		// this.actor = actor.asString;
		// this.netAddr.sendMsg("/create", actor, anim);
		// this.netAddr.ip.debug("net IP");
		// this.netAddr.port.debug("net port");
		// this.netAddr.sendMsg("/list/assets");
	}

	send { |...args|
		var n = NetAddr("localhost", 56101);
		n.sendMsg(*args);
	}

	doesNotUnderstand { |selector ...args|
		var msg = ["/"++selector]++args;
		msg.debug("msg");
		// selector.debug((this.class ++ " does not understand method").asString);

        // super.findRespondingMethodFor(selector);
		// super.selector(args);

		this.netAddr.sendMsg(*msg);
	}

}