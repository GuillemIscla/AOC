use crate::year_2025::day_001::rotation::Rotation;

pub struct Dial {
    pub position: i32,
}

impl Dial {
    pub fn new() -> Dial {
        Dial { position: 50 }
    }

    pub fn rotate(&mut self, rotation: Rotation) -> i32 {
        let before = self.position;
        let raw_result = if rotation.is_left {
            self.position - rotation.distance
        } else {
            self.position + rotation.distance
        };
        self.position = (raw_result) % 100; // Got a number between -99 and 99
        self.position = (self.position + 100) % 100; // Got a number between 0 and 99

        // If I was going left and I end up in a higher dial need to add one
        // Same if I was going right and end up in a lower dial position
        let extra_for_position = if (before < self.position && rotation.is_left)
            || (before > self.position && !rotation.is_left)
        {
            1
        } else {
            0
        };

        // If I start at 0 and I rotate left, means I already did the click before, need to
        // compensate that 1 that is going to come at extra_for_position
        let extra_for_start_at_zero = if before == 0 && rotation.is_left {
            -1
        } else {
            0
        };

        // If I was going backwards and end at zero, the dial position is low, so will not account
        // the click in extra_for_position but actually it just clicked
        let extra_for_end_at_zero = if self.position == 0 && rotation.is_left {
            1
        } else {
            0
        };

        (rotation.distance / 100)
            + extra_for_position
            + extra_for_start_at_zero
            + extra_for_end_at_zero
    }
}
