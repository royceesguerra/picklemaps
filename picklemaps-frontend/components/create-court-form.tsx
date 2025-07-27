import { useFormContext } from 'react-hook-form';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import { Photo } from '@/domain/domain';
import { Trash2 } from 'lucide-react';

const daysOfWeek = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];

interface CourtFormProps {
  uploadPhoto: (file: File, caption?: string) => Promise<Photo>;
  error?: string;
}

export default function CourtForm({ uploadPhoto, error }: CourtFormProps) {
  const methods = useFormContext();
  const { register, setValue } = methods;
  const [previews, setPreviews] = useState<string[]>([]);

  useEffect(() => {
    const existingPhotoIds = methods.getValues('photos') || [];
    if (existingPhotoIds.length > 0) {
      const existingPreviews = existingPhotoIds.map((photoId: string) => `/api/photos/${photoId}`);
      setPreviews(existingPreviews);
    }
  }, [methods]);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    const uploadedPhotos = await Promise.all(files.map(photo => uploadPhoto(photo)));

    const existingPhotoIds = methods.getValues('photos') || [];
    const photoIds = [...existingPhotoIds, ...uploadedPhotos.map(photo => photo.url)];

    console.log('PhotoIDs ' + photoIds);
    setValue('photos', photoIds);

    const newPreviews = files.map(file => URL.createObjectURL(file));
    setPreviews([...previews, ...newPreviews]);
  };

  const handleRemovePhoto = (indexToRemove: number) => {
    const updatedPreviews = previews.filter((_, index) => index !== indexToRemove);
    setPreviews(updatedPreviews);

    const existingPhotoIds = methods.getValues('photos') || [];
    const updatedPhotoIds = existingPhotoIds.filter((_, index) => index !== indexToRemove);
    setValue('photos', updatedPhotoIds);

    if (previews[indexToRemove].startsWith('blob:')) {
      URL.revokeObjectURL(previews[indexToRemove]);
    }
  };

  return (
    <div className="space-y-8">
      {/* Basic Info Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Basic Information</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="name">Court Name</Label>
            <Input id="name" {...register('name', { required: true })} />
          </div>
          <div>
            <Label htmlFor="courtType">Court Type</Label>
            <Input id="courtType" {...register('courtType', { required: true })} />
          </div>
          <div>
            <Label htmlFor="surfaceType">Surface Type</Label>
            <Input id="surfaceType" {...register('surfaceType', { required: true })} />
          </div>
          <div>
            <Label htmlFor="contactInformation">Contact Information</Label>
            <Input
              id="contactInformation"
              {...register('contactInformation', { required: true })}
            />
          </div>
        </div>
      </div>

      {/* Address Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Address</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="streetNumber">Street Number</Label>
            <Input id="streetNumber" {...register('address.streetNumber', { required: true })} />
          </div>
          <div>
            <Label htmlFor="streetName">Street Name</Label>
            <Input id="streetName" {...register('address.streetName', { required: true })} />
          </div>
          <div>
            <Label htmlFor="Court">Barangay</Label>
            <Input id="barangay" {...register('address.barangay', { required: true })} />
          </div>
          <div>
            <Label htmlFor="city">City</Label>
            <Input id="city" {...register('address.city', { required: true })} />
          </div>
          <div>
            <Label htmlFor="province">Province</Label>
            <Input id="province" {...register('address.province', { required: true })} />
          </div>
          <div>
            <Label htmlFor="postalCode">Postal Code</Label>
            <Input id="postalCode" {...register('address.postalCode', { required: true })} />
          </div>
        </div>
      </div>

      {/* Operating Hours Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Operating Hours</h2>
        <div className="space-y-4">
          {daysOfWeek.map(day => (
            <div key={day} className="flex items-center space-x-4">
              <Label className="w-24 capitalize">{day}</Label>
              <div className="flex-1 space-x-2">
                <Input
                  type="time"
                  className="w-[120px] inline-block"
                  onChange={e => {
                    if (e.target.value) {
                      const currentValues = methods.getValues(`operatingHours.${day}`) || {
                        openTime: '',
                        closeTime: '',
                      };
                      setValue(`operatingHours.${day}`, {
                        openTime: e.target.value,
                        closeTime: currentValues.closeTime || '',
                      });
                    } else {
                      const currentValues = methods.getValues(`operatingHours.${day}`);
                      if (!currentValues || !currentValues.closeTime) {
                        setValue(`operatingHours.${day}`, null);
                      } else {
                        setValue(`operatingHours.${day}`, {
                          openTime: '',
                          closeTime: currentValues.closeTime,
                        });
                      }
                    }
                  }}
                  defaultValue={methods.getValues(`operatingHours.${day}`)?.openTime || ''}
                />

                <span>to</span>

                <Input
                  type="time"
                  className="w-[120px] inline-block"
                  onChange={e => {
                    if (e.target.value) {
                      const currentValues = methods.getValues(`operatingHours.${day}`) || {
                        openTime: '',
                        closeTime: '',
                      };
                      setValue(`operatingHours.${day}`, {
                        openTime: currentValues.openTime || '',
                        closeTime: e.target.value,
                      });
                    } else {
                      const currentValues = methods.getValues(`operatingHours.${day}`);
                      if (!currentValues || !currentValues.openTime) {
                        setValue(`operatingHours.${day}`, null);
                      } else {
                        setValue(`operatingHours.${day}`, {
                          openTime: currentValues.openTime,
                          closeTime: '',
                        });
                      }
                    }
                  }}
                  defaultValue={methods.getValues(`operatingHours.${day}`)?.closeTime || ''}
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Photos Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Court Photos</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="photos">Upload Court Photos</Label>
            <Input
              id="photos"
              type="file"
              accept="image/*"
              multiple
              onChange={handleFileChange}
              className="hidden"
            />
            <Button
              type="button"
              variant="outline"
              onClick={() => document.getElementById('photos')?.click()}
            >
              Select Photos
            </Button>
          </div>
          {previews.length > 0 && (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mt-4">
              {previews.map((preview, index) => (
                <div key={index} className="relative aspect-square group">
                  <Image
                    src={preview || '/placeholder.svg'}
                    alt={`Preview ${index + 1}`}
                    fill
                    className="object-cover rounded-md"
                  />
                  {/* Trash can overlay */}
                  <div className="absolute inset-0 bg-black bg-opacity-40 opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center rounded-md">
                    <button
                      type="button"
                      className="p-2 bg-red-500 rounded-full text-white hover:bg-red-600 transition-colors"
                      onClick={() => handleRemovePhoto(index)}
                      aria-label="Remove photo"
                    >
                      <Trash2 size={20} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {error && <p>Error: {error}</p>}

      {/* Submit Button */}
      <Button type="submit" className="w-full">
        Submit Court
      </Button>
    </div>
  );
}
