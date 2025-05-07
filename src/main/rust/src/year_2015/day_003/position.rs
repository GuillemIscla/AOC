use crate::year_2015::day_003::direction::Direction;

#[derive(Debug, Clone, Hash, Eq, PartialEq)]
pub struct Position {
    pub x: i32,
    pub y: i32,
}

impl Position {
    pub fn new(x: i32, y: i32) -> Position {
        Position { x, y }
    }

    pub fn add(&self, direction: Direction) -> Position {
        match direction {
            Direction::North => Position::new(self.x, self.y + 1),
            Direction::South => Position::new(self.x, self.y - 1),
            Direction::East => Position::new(self.x + 1, self.y),
            Direction::West => Position::new(self.x - 1, self.y),
        }
    }
}
