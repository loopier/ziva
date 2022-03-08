// Live coding in SuperCollider made easy.

// This file defines custom event types to be used with Pbind and a general
// class to manage resources.
// Some parts are inspired by SuperDirt by Julian Rohrhuber

// (C) 2022 Roger Pibernat

// Ziva is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 2 of the License, or (at your
// option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

ZivaEventTypes {
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

Ziva {
	classvar <> server;
	classvar <> samplesDir;

	*start { |inputChannels = 2, outputChannels = 2, server = nil|
		^this.boot(inputChannels, outputChannels, server);
	}

	*boot { |inputChannels = 2, outputChannels = 2, server = nil,
		numBuffers = 16, memSize = 32, maxNodes = 32|
		server = server ? Server.default;
		this.server = server;
		this.serverOptions(this.server, inputChannels, outputChannels, numBuffers, memSize, maxNodes);
		this.server.waitForBoot{
			Ziva.loadSounds;
		};
		^this.server;
	}

	*serverOptions { |server = nil, inputChannels = 2, outputChannels = 2, numBuffers = 16, memSize = 32, maxNodes = 32|
		server = server ? Server.default;
		server.options.numBuffers = 1024 * numBuffers; // increase this if you need to load more samples
		server.options.memSize = 8192 * memSize; // increase this if you get "alloc failed" messages
		server.options.maxNodes = 1024 * maxNodes;
		server.options.numInputBusChannels = inputChannels;
		server.options.numOutputBusChannels = outputChannels;
	}

	*scope { |alwaysOnTop = true|
		server.scope(2).style_(2)
		.window.bounds_(Rect( 145 + 485, 0, 200, 250))
		.alwaysOnTop_(alwaysOnTop);
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
	*loadSynths { |path|
		// load synthdesclib
		// "loading synths".debug;
		var filePaths;
		path = path ?? { "../synths".resolveRelative };
		filePaths = pathMatch(standardizePath(path +/+ "*"));
		filePaths.do { |filepath|
			if(filepath.splitext.last == "scd") {
				(dirt:this).use { filepath.load }; "loading synthdefs in %\n".postf(filepath)
			}
		}
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

	/// \brief return a list of the controls for the given synth
    *synthControls { |synth|
        var controls = List();
        SynthDescLib.global.at(synth).controls.do{ |ctl|
            controls.add([ctl.name, ctl.defaultValue]);
        }
        ^controls
    }

	/// \brief list available methods
	// *help { |class|
	// 	class = class ? Pchain;
	// 	class.methods.collect(_.postln);
	// }

	*controls { |synth|
        "% controls".format(synth).postln;
        this.synthControls(synth).collect(_.postln)
	}
}