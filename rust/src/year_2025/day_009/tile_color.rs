#[derive(Clone, PartialEq)]
pub enum TileColor {
    Red,
    Green,
    Neither,
    NotPainted,
}

impl TileColor {
    pub fn as_char(&self) -> char {
        match self {
            TileColor::Red => '#',
            TileColor::Green => 'X',
            TileColor::Neither => '.',
            TileColor::NotPainted => ' ',
        }
    }
}
