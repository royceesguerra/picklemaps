'use client';

import { useForm, FormProvider } from 'react-hook-form';
import { Card, CardContent } from '@/components/ui/card';
import { useAppContext } from '@/providers/app-context-provider';
import { CreateCourtRequest, Photo } from '@/domain/domain';
import CreateCourtForm from '@/components/create-court-form';
import { useState } from 'react';
import axios from 'axios';

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

  const uploadPhoto = async (file: File, caption?: string): Promise<Photo> => {
    if (null == apiService) {
      throw Error('API Service not available!');
    }
    return apiService.uploadPhoto(file, caption);
  };

  const onSubmit = async (data: FormData) => {
    console.log('Form submitted:', data);

    try {
      const createCourtRequest: CreateCourtRequest = {
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

      await apiService.createCourt(createCourtRequest);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 400) {
          const errorData = err.response.data?.message;
          setError(errorData);
        } else {
          setError(`API Error: ${err.response?.status}: ${err.response?.data}`);
        }
      } else {
        setError(error);
      }
    }
  };

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Create a New Court</h1>
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
