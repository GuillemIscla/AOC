use crate::year_2025::day_009::tile::Tile;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> Vec<Tile> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<Tile>> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_009.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = "7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"
            .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    let tiles = content
        .lines()
        .map(Tile::new)
        .collect::<io::Result<Vec<Tile>>>()?;

    Ok(tiles)
}
