/* tslint:disable:no-trailing-whitespace */
export class ListingRequest {
  id : string ;
  title : string ;
  address: string;
  description : string;
  listingImages : string[] ;
  price: string;
  city: string;
  area: string;
  occupants: string;
  facilities: string[];
  type: string;
  bhk: string;
  createdBy: string;

  constructor(id: string , title : string , address : string , price  : string , description : string ,
              city : string , area : string , occupants : string , facilities : string[] ,
              type : string , bhk : string , listingImages : string[], createdBy : string)
  {
    this.id = id;
    this.price = price;
    this.city = city;
    this.area = area;
    this.occupants = occupants;
    this.facilities = facilities;
    this.type = type;
    this.bhk = bhk;
    this.listingImages = listingImages;
    this.title = title;
    this.address = address;
    this.description = description;
    this.createdBy = createdBy;

  }


}
