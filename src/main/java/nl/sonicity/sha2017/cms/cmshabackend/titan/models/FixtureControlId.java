package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

/**
 * Created by hugo on 24/06/2017.
 */
public enum FixtureControlId {
    Dimmer(0x10), // The intensity control of a fixture.
    Pan(0x20), // The horizontal position control for a fixture.
    Tilt(0x30), // The Vertical position control of a fixture.
    Cyan(0x40), // Cyan component used in subtracted colour mixing
    CyanAdditive(0x41), // The amount of cyan in the output colour of a fixture.
    Magenta(0x50), // Magenta component used in subtracted colour mixing
    MagentaAdditive(0x51), // The amount of magenta in the output colour of a fixture.
    Yellow(0x60), // Yellow component used in subtracted colour mixing
    YellowAdditive(0x61), // The amount of yellow in the output colour of a fixture.
    Red(0xf0), // The amount of red in the output colour of a fixture.
    Green(0x100), // The amount of green in the output colour of a fixture.
    Blue(0x110), // The amount of blue in the output colour of a fixture.
    Amber(0x111), // The amount of Amber in the output colour of a fixture.
    White(0x112), // The amount of White in the output colour of a fixture.
    WarmWhite(0x113), // The amount of Warm White in the output colour of a fixture.
    ColdWhite(0x114), // The amount of Cold White in the output colour of a fixture.
    CTO(0x115), // Colour temperature correction channel.
    UV(0x116), // UV Colour Channel
    Lime(0x117), // Lime Colour Channel
    Colour1(0x70), // Colour wheel 1
    Colour2(0x80), // Colour wheel 2
    Gobo1(0x90), // Gobo wheel 1
    Gobo2(0xa0), // Gobo wheel 2
    Gobo1Rotate(0xb0), // Gobo wheel 1 rotation
    Gobo2Rotate(0xc0), // Gobo wheel 2 rotation
    Iris(0xd0), // Fixture iris
    Focus(0xe0), // Fixture focus
    Zoom(0xe1), // Fixture Zoom
    KeystoneTopLeftX(0x130), // Keystone top left x
    KeystoneTopLeftY(0x140), // Keystone top left Y
    KeystoneTopRightX(0x150), // Keystone top right x
    KeystoneTopRightY(0x160), // Keystone top right y
    KeystoneBottomRightX(0x170), // Keystone bottom right x
    KeystoneBottomRightY(0x180), // Keystone bottom right y
    KeystoneBottomLeftX(0x190), // Keystone bottom left X
    KeystoneBottomLeftY(0x1a0), // Keystone bottom left Y
    Keystone1(0x2e0), // Position of the top keystone edge
    Keystone2(0x2f0), // Position of the right keystone edge
    Keystone3(0x300), // Position of the bottom keystone edge
    Keystone4(0x310), // Position of the left keystone edge
    Keystone1Rotation(0x320), // Rotation of the top keystone edge
    Keystone2Rotation(0x330), // Rotation of the right keystone edge
    Keystone3Rotation(0x340),
    Keystone4Rotation(0x350), // Rotation to the left keystone edge
    BladeTopLeft(0x1b0), // Top blade left corner
    BladeTopRight(0x1c0), // Top blade right corner
    BladeRightTop(0x1d0), // Right blade top corner
    BladeRightBottom(0x1e0), // Right blade bottom corner
    BladeBottomRight(0x1f0), // Bottom blade right corner
    BladeBottomLeft(0x200), // Bottom blade left corner
    BladeLeftBottom(0x210), // Left blade bottom corner
    BladeLeftTop(0x220), // Left blade top corner
    Blade1(0x230), // Blade 1 position
    Blade2(0x240), // Blade 2 position
    Blade3(0x250), // Blade 3 postion
    Blade4(0x260), // Blade 4 position
    Blade1Rotation(0x270), // Blade 1 Rotation
    Blade2Rotation(0x280), // Blade 2 Rotation
    Blade3Rotation(0x290), // Blade 3 Rotation
    Blade4Rotation(0x2a0), // Blade 4 Rotation
    BladeAllRotation(0x2b0), // Blade system overall rotate
    KeystoneXRatio(0x2c0), // Keystone x ratio
    KeystoneYRatio(0x2d0), // Keystone y ratio
    ImageZoom(0x380), // Image Size
    AspectRatio(0x381), // Aspect Ratio
    AspectMode(0x382), // Aspect Mode
    MTC_Hour(0x383), // MTC Hour
    MTC_Minute(0x384), // MTC Minute
    MTC_Second(0x385), // MTC Second
    MTC_Frame(0x386), // MTC Frame
    Play_Mode(0x387), // Play Mode
    Play_Speed(0x388), // Play Speed
    In_Point(0x389), // In Point
    Out_Point(0x38a); // Out Point

    private int value;

    FixtureControlId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
