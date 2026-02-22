use regex::Regex;
use std::str::FromStr;
use std::sync::LazyLock;

#[derive(Debug)]
pub struct Rotation {
    pub is_left: bool,
    pub distance: i32,
}

impl std::fmt::Display for Rotation {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(
            f,
            "Rotation ( is_left: {}, distance: {} )",
            self.is_left, self.distance
        )
    }
}

static RE: LazyLock<Regex> = LazyLock::new(|| Regex::new(r"(L|R)([0-9]+)").unwrap());

impl FromStr for Rotation {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if let Some(caps) = RE.captures(s) {
            let is_left = &caps[1] == "L";
            let distance: i32 = caps[2]
                .parse()
                .map_err(|_| format!("'{}' is not parseable into an int", &caps[2]))?;
            Ok(Rotation { is_left, distance })
        } else {
            Err(format!(
                "I got the regex '{}' not matching the line '{}'",
                "(L|R)([0-9]+)", s
            ))
        }
    }
}
