= Živa

== Usage
=== Quick start

    Ziva.boot; // boot the server
    ~lola s: \tri >>>.1 1 // play a variable triangle wave synth on channel 1

=== Creating a sound

    ~lola s: \tri >>>.1 1

- `~lola` is the name of your instrument. It can be anything, but has to be lead by a `~`.
- `s:` is the method that sets the `sound` that the instrument will play.
- `\tri` is the sound name. See `Ziva.sounds`
- `>>>.1 1` sends this instrument to the mixer. The first number -- after the dot `.` --  is the `channel number`. The second is the `gain`.
