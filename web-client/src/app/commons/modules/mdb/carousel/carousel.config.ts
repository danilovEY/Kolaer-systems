import { Injectable } from '@angular/core';

@Injectable()
export class MdbCarouselConfig {
  /** Default interval of auto changing of slides */
  public interval = 5000;

  /** Is loop of auto changing of slides can be paused */
  public noPause = false;

  /** Is slides can wrap from the last to the first slide */
  public noWrap = false;

  public keyboard = false;
}
