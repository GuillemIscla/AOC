use std::io;

pub struct Device {
    pub id: String,
    pub go_to: Vec<String>,
}

impl Device {
    pub fn new(raw: &str) -> io::Result<Self> {
        let split = raw
            .split(": ")
            .map(|str| str.to_string())
            .collect::<Vec<String>>();

        let id = split[0].clone();

        let go_to = split[1]
            .split(" ")
            .map(|str| str.to_string())
            .collect::<Vec<String>>();

        Ok(Device { id, go_to })
    }
}
