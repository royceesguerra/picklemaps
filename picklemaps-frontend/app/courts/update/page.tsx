'use client';

import { useForm, FormProvider } from 'react-hook-form';
import { Card, CardContent } from '@/components/ui/card';
import { useAppContext } from '@/providers/app-context-provider';
import { CreateCourtRequest, Photo } from '@/domain/domain';
import CreateCourtForm from '@/components/create-court-form';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter, useSearchParams } from 'next/navigation';

type FormData = {
  name: string;
  courtType: string;
  surfaceType: string;
  contactInformation: string;
  address: {
    streetNumber: string;
    streetName: string;
    barangay?: string;
    city: string;
    province: string;
    postalCode: string;
  };
  operatingHours: {
    monday: { openTime: string; closeTime: string } | undefined;
    tuesday: { openTime: string; closeTime: string } | undefined;
    wednesday: { openTime: string; closeTime: string } | undefined;
    thursday: { openTime: string; closeTime: string } | undefined;
    friday: { openTime: string; closeTime: string } | undefined;
    saturday: { openTime: string; closeTime: string } | undefined;
    sunday: { openTime: string; closeTime: string } | undefined;
  };
  photos: string[];
};

export default function CreateCourtPage() {
  const { apiService } = useAppContext();
  const [error, setError] = useState<string | undefined>();
  const [loading, setLoading] = useState<boolean>(true);
  const searchParams = useSearchParams();

  const courtId = searchParams.get('id');
  const router = useRouter();

  const methods = useForm<FormData>({
    defaultValues: {
      name: '',
      courtType: '',
      surfaceType: '',
      contactInformation: '',
      address: {
        streetNumber: '',
        streetName: '',
        barangay: '',
        city: '',
        province: '',
        postalCode: '',
      },
      operatingHours: {
        monday: undefined,
        tuesday: undefined,
        wednesday: undefined,
        thursday: undefined,
        friday: undefined,
        saturday: undefined,
        sunday: undefined,
      },
      photos: [],
    },
  });

  useEffect(() => {
    const doUseEffect = async () => {
      if (!apiService) {
        return;
      }

      setLoading(true);
      setError(undefined);

      if (!courtId) {
        setError('Court ID must be provided');
        setLoading(false);
        return;
      }

      try {
        const court = await apiService.getCourt(courtId);

        methods.reset({
          name: court.name,
          courtType: court.courtType,
          surfaceType: court.surfaceType,
          contactInformation: court.contactInformation,
          address: {
            streetNumber: court.address.streetNumber,
            streetName: court.address.streetName,
            barangay: court.address.barangay || '',
            city: court.address.city,
            province: court.address.province,
            postalCode: court.address.postalCode,
          },
          operatingHours: {
            monday: court.operatingHours.monday,
            tuesday: court.operatingHours.tuesday,
            wednesday: court.operatingHours.wednesday,
            thursday: court.operatingHours.thursday,
            friday: court.operatingHours.friday,
            saturday: court.operatingHours.saturday,
            sunday: court.operatingHours.sunday,
          },
          photos: court.photos?.map(photo => photo.url) || [],
        });
      } catch (err) {
        if (axios.isAxiosError(err)) {
          if (err.response?.status === 404) {
            setError('Court not found');
          } else {
            setError(`Error fetching court: ${err.response?.status}: ${err.response?.data}`);
          }
        } else {
          setError('Error fetching court data');
        }
      } finally {
        setLoading(false);
      }
    };

    doUseEffect();
  }, [apiService, courtId, methods]);

  const uploadPhoto = async (file: File, caption?: string): Promise<Photo> => {
    if (null == apiService) {
      throw Error('API Service not available!');
    }
    return apiService.uploadPhoto(file, caption);
  };

  const onSubmit = async (data: FormData) => {
    console.log('Form submitted:', data);

    try {
      const updateCourtRequest: CreateCourtRequest = {
        name: data.name,
        courtType: data.courtType,
        surfaceType: data.surfaceType,
        contactInformation: data.contactInformation,
        address: data.address,
        operatingHours: data.operatingHours,
        photoIds: data.photos,
      };

      if (null == apiService) {
        throw Error('API Service not available!');
      }

      setError(undefined);

      if (courtId) {
        await apiService.updateCourt(courtId, updateCourtRequest);
        router.push('/');
      } else {
        await apiService.createCourt(updateCourtRequest);
        router.push('/');
      }
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 400) {
          const errorData = err.response.data?.message;
          setError(errorData);
        } else {
          setError(`API Error: ${err.response?.status}: ${err.response?.data}`);
        }
      } else {
        setError(String(err));
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-[100vh] h-full flex items-center justify-center">
        <p>Loading 🍝</p>
      </div>
    );
  }

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Update a Court</h1>
      <Card>
        <CardContent className="pt-6">
          <FormProvider {...methods}>
            <form onSubmit={methods.handleSubmit(onSubmit)}>
              <CreateCourtForm uploadPhoto={uploadPhoto} error={error} />
            </form>
          </FormProvider>
        </CardContent>
      </Card>
    </div>
  );
}
