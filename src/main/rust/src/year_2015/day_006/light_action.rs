use std::io::{self, Error, ErrorKind};

#[derive(PartialEq, Debug)]
pub enum LightAction {
    TurnOn,
    TurnOff,
    Toggle,
}

impl LightAction {
    pub fn from_raw(light_action_raw: &str) -> io::Result<LightAction> {
        match light_action_raw {
            "turn on" => Ok(LightAction::TurnOn),
            "turn off" => Ok(LightAction::TurnOff),
            "toggle" => Ok(LightAction::Toggle),
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!("Invalid light_action symbol: {}", light_action_raw),
            )),
        }
    }
}
