use super::rolls_diagram::RollsDiagram;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> RollsDiagram {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<RollsDiagram> {
    match sample {
        None => {
            let file_path = "inputs/year_2025/day_004.txt";
            let mut file: File = File::open(file_path)?;
            let mut content = String::new();
            file.read_to_string(&mut content)?;
            Ok(RollsDiagram::new(content))
        }

        Some(1) => {
            let content = "..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@."
                .to_string();
            Ok(RollsDiagram::new(content))
        }
        _ => Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Sample received number is wrong",
        )),
    }
}
