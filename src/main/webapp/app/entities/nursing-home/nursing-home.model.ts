export interface INursingHome {
  id?: number;
  name?: string | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  country?: string | null;
}

export class NursingHome implements INursingHome {
  constructor(
    public id?: number,
    public name?: string | null,
    public streetAddress?: string | null,
    public postalCode?: string | null,
    public city?: string | null,
    public country?: string | null
  ) {}
}

export function getNursingHomeIdentifier(nursingHome: INursingHome): number | undefined {
  return nursingHome.id;
}
