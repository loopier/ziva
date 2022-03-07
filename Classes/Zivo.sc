ZivoEventTypes {
	*new {
		Event.addEventType(\sample, { |server|
			~sound = ~sound ? [];
			~n = ~n ? 0;
			~channels = ~channels ? 2;
			~instrument = [\playbufm, \playbuf][~channels-1];
			~buf = ~sound.at(~n.mod(~sound.size));
			// TODO: !!! ~note modifies rate
			~type = \note;
			currentEnvironment.play;
		},
			// defaults
			(legato: 1)
		);
	}
}

Zivo {
	classvar <> server;
	classvar <> samplesDir;

	*boot { arg inputChannels = 2, outputChannels = 2, server = Server.default,
		numBuffers = 16, memSize = 32, maxNodes = 32;
		this.server = server;
		this.serverOptions(this.server, inputChannels, outputChannels, numBuffers, memSize, maxNodes);
		this.server.waitForBoot{
			Zivo.loadSounds;
		};
		^this.server;
	}

	*serverOptions { arg server = Server.default, inputChannels = 2, outputChannels = 2, numBuffers = 16, memSize = 32, maxNodes = 32;
		server.options.numBuffers = 1024 * numBuffers; // increase this if you need to load more samples
		server.options.memSize = 8192 * memSize; // increase this if you get "alloc failed" messages
		server.options.maxNodes = 1024 * maxNodes;
		server.options.numInputBusChannels = inputChannels;
		server.options.numOutputBusChannels = outputChannels;
	}

	*scope {
		server.scope(2).style_(2)
		.window.bounds_(Rect( 145 + 485, 0, 200, 250))
		.alwaysOnTop_(true);
	}

	/// \brief load samples
	*loadSounds {
		"loading sounds".debug;
		this.loadSamples;
		this.loadSynths;
	}

	/// \biref load dir contents
	/// \description a directory with symlinks should work
	*loadSamples {
		// load dir contents
		// a directory with symlinks should work
		"loading samples".debug;
	}

	/// \brief load synthdefs
	*loadSynths {
		// load synthdesclib
		"loading synths".debug;
	}

	/// \brief Return a list of all compiled SynthDef names
	*synthDefList {
		var names = SortedList.new;

		SynthDescLib.global.synthDescs.do { |desc|
			if(desc.def.notNil) {
				// Skip names that start with "system_"
				if ("^[^system_|pbindFx_]".matchRegexp(desc.name)) {
					names.add(desc.name);
				};
			};
		};

		^names;
	}

	/// \brief list synth names
	*synths {
		this.synthDefList.collect(_.postln);
	}

	/// \brief post synths and samples
	*sounds {
		// list synths
		// list samples - name(items)
		"listing sounds".debug;
	}

    *listSynthControls { |synth|
        "% controls".format(synth).postln;
        this.synthControls(synth).collect(_.postln)
    }

	*controls { |synth|
		this.listSynthControls(synth);
	}
}