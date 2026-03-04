use super::tachyon_diagram::TachyonDiagram;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> TachyonDiagram {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<TachyonDiagram> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_007.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = ".......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
..............."
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    TachyonDiagram::new(content)
}
