// Live coding in SuperCollider made easy.

// This file defines custom event types to be used with Pbind.

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
		Event.addEventType(\zsample, { |server|
			~sound = ~sound ? [];
			~n = ~n ? 0;
			~channels = ~channels ? 2;
			~instrument = [\playbufm, \playbuf][~channels-1];
			// ~instrument = [\zsamplermono, \zsampler][~channels-1];
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
