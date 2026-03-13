use crate::year_2025::day_011::device::Device;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> Vec<Device> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<Device>> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_011.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = "aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out"
                .to_string();
        }
        Some(2) => {
            content = "svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out"
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    let devices = content
        .lines()
        .map(Device::new)
        .collect::<io::Result<Vec<Device>>>()?;

    Ok(devices)
}
